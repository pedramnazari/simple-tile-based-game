package de.pedramnazari.simpletbg.savegame.adapters.game;

import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.drivers.GameInitializer;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.savegame.application.LoadedGame;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateRestorer;
import de.pedramnazari.simpletbg.savegame.domain.*;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;

import java.util.*;
import java.util.stream.Collectors;

public class GameWorldStateRestorerAdapter implements ActiveGameStateRestorer {

    @Override
    public LoadedGame restore(GameSnapshot snapshot) {
        GameContext.resetInstance();
        GameMapDefinition definition = GameMaps.findById(snapshot.mapId())
                .orElseThrow(() -> new IllegalStateException("Unknown map id: " + snapshot.mapId()));

        GameWorldController controller = GameInitializer.initAndStartGame(definition);
        GameWorldService gameWorldService = controller.getGameWorldService();
        gameWorldService.setCurrentMapIndex(snapshot.currentMapIndex());

        GameContext context = GameContext.getInstance();
        HeroService heroService = (HeroService) context.getHeroService();
        IHero hero = heroService.getHero();
        applyHeroState(hero, snapshot.hero());

        IItemService itemService = context.getItemService();
        restoreWorldItems(itemService, snapshot.worldItems());
        restoreHeroInventory(heroService, itemService, snapshot.hero(), snapshot.inventory());

        restoreEnemies((EnemyService) context.getEnemyService(), heroService, snapshot.enemies());
        restoreQuest(gameWorldService.getQuest(), snapshot.quest());

        return new LoadedGame(controller, definition);
    }

    private void applyHeroState(IHero hero, HeroState state) {
        hero.setX(state.x());
        hero.setY(state.y());
        hero.setHealth(state.health());
        hero.setAttackingPower(state.attackingPower());
        hero.setWeapon(null);
        hero.setRing(null);

        // clear inventory
        for (IItem existing : new ArrayList<>(hero.getInventory().getItems())) {
            hero.getInventory().removeItem(existing);
        }
    }

    private void restoreHeroInventory(HeroService heroService,
                                      IItemService itemService,
                                      HeroState heroState,
                                      InventoryState inventoryState) {
        IHero hero = heroService.getHero();

        heroState.weaponType().ifPresent(type -> {
            IItem weapon = withdrawItem(itemService, type);
            if (weapon instanceof IWeapon w) {
                hero.setWeapon(w);
            }
        });
        heroState.ringType().ifPresent(type -> {
            IItem ringItem = withdrawItem(itemService, type);
            if (ringItem instanceof IRing ring) {
                hero.setRing(ring);
            }
        });

        for (InventoryItemState itemState : inventoryState.items()) {
            IItem item = withdrawItem(itemService, itemState.itemType());
            if (item != null) {
                hero.getInventory().addItem(item);
            }
        }
    }

    private IItem withdrawItem(IItemService itemService, int itemType) {
        for (IItem item : new ArrayList<>(itemService.getItems())) {
            if (item.getType() == itemType) {
                itemService.removeItem(item);
                return item;
            }
        }
        return null;
    }

    private void restoreWorldItems(IItemService itemService, List<WorldItemState> desiredItems) {
        List<WorldItemState> remaining = new ArrayList<>(desiredItems);
        for (IItem existing : new ArrayList<>(itemService.getItems())) {
            WorldItemState matched = remaining.stream()
                    .filter(state -> state.itemType() == existing.getType()
                            && state.x() == existing.getX()
                            && state.y() == existing.getY())
                    .findFirst()
                    .orElse(null);
            if (matched != null) {
                remaining.remove(matched);
            } else {
                itemService.removeItem(existing);
            }
        }
    }

    private void restoreEnemies(EnemyService enemyService, HeroService heroService, List<EnemyState> enemyStates) {
        CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroService);
        List<IEnemy> enemies = enemyStates.stream().map(state -> {
            IEnemy enemy = enemyFactory.createElement(state.enemyType(), state.x(), state.y());
            enemy.setHealth(state.health());
            enemy.setAttackingPower(state.attackingPower());
            return enemy;
        }).collect(Collectors.toList());
        enemyService.reset(enemies);
    }

    private void restoreQuest(Quest quest, QuestState questState) {
        List<?> objectives = quest.getObjectives();
        for (QuestObjectiveState objectiveState : questState.objectives()) {
            QuestObjective objective = objectives.stream()
                    .map(QuestObjective.class::cast)
                    .filter(obj -> obj.getDescription().equals(objectiveState.description()))
                    .findFirst()
                    .orElse(null);
            if (objective != null && objectiveState.completed()) {
                objective.complete();
            }
        }
    }
}
