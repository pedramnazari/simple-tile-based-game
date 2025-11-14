package de.pedramnazari.simpletbg.savegame.adapters.game;

import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.drivers.GameInitializer;
import de.pedramnazari.simpletbg.drivers.GameInitializer.GameRuntime;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.savegame.domain.*;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;

import java.util.*;
import java.util.stream.Collectors;

public class GameSessionRestorer {

    public GameRuntime restore(GameSnapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");
        GameContext.resetInstance();

        GameMapDefinition mapDefinition = GameMaps.findById(snapshot.mapId())
                .orElseThrow(() -> new IllegalStateException("Unknown map id: " + snapshot.mapId()));

        GameRuntime runtime = GameInitializer.initAndStartGame(mapDefinition);
        GameWorldService gameWorldService = runtime.gameWorldService();
        gameWorldService.stop();
        String mapIndex = snapshot.currentMapIndex() != null ? snapshot.currentMapIndex() : snapshot.mapId();
        gameWorldService.setCurrentMapIndex(mapIndex);

        HeroService heroService = (HeroService) gameWorldService.getHeroService();
        applyHeroState(heroService.getHero(), snapshot.hero());

        IItemService itemService = gameWorldService.getItemService();
        restoreWorldItems(itemService, runtime.itemFactory(), snapshot.worldItems());
        restoreHeroInventory(heroService, itemService, runtime.itemFactory(), snapshot.hero(), snapshot.inventory());

        restoreEnemies((EnemyService) gameWorldService.getEnemyService(), heroService, runtime.collisionDetectionService(), snapshot.enemies());
        restoreQuest(gameWorldService.getQuest(), snapshot.quest());

        gameWorldService.start();
        return runtime;
    }

    private void applyHeroState(IHero hero, HeroState state) {
        hero.setX(state.x());
        hero.setY(state.y());
        hero.setHealth(state.health());
        hero.setAttackingPower(state.attackingPower());
        hero.setWeapon(null);
        hero.setRing(null);
        hero.getInventory().getItems().stream().collect(Collectors.toList()).forEach(hero.getInventory()::removeItem);
    }

    private void restoreHeroInventory(HeroService heroService,
                                      IItemService itemService,
                                      DefaultItemFactory itemFactory,
                                      HeroState heroState,
                                      InventoryState inventoryState) {
        IHero hero = heroService.getHero();

        heroState.weaponType().ifPresent(type -> {
            IItem weapon = withdrawItem(itemService, itemFactory, type, heroState.x(), heroState.y());
            if (weapon instanceof IWeapon w) {
                hero.setWeapon(w);
            }
        });

        heroState.ringType().ifPresent(type -> {
            IItem ring = withdrawItem(itemService, itemFactory, type, heroState.x(), heroState.y());
            if (ring instanceof IRing r) {
                hero.setRing(r);
            }
        });

        for (InventoryItemState itemState : inventoryState.items()) {
            IItem item = withdrawItem(itemService, itemFactory, itemState.itemType(), heroState.x(), heroState.y());
            if (item != null) {
                hero.getInventory().addItem(item);
            }
        }
    }

    private IItem withdrawItem(IItemService itemService,
                               DefaultItemFactory itemFactory,
                               int itemType,
                               int fallbackX,
                               int fallbackY) {
        for (IItem item : new ArrayList<>(itemService.getItems())) {
            if (item.getType() == itemType) {
                itemService.removeItem(item);
                return item;
            }
        }
        return itemFactory.createElement(itemType, fallbackX, fallbackY);
    }

    private void restoreWorldItems(IItemService itemService,
                                   DefaultItemFactory itemFactory,
                                   List<WorldItemState> desiredItems) {
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

        for (WorldItemState state : remaining) {
            IItem item = itemFactory.createElement(state.itemType(), state.x(), state.y());
            itemService.addItem(item);
        }
    }

    private void restoreEnemies(EnemyService enemyService,
                                HeroService heroService,
                                CollisionDetectionService collisionDetectionService,
                                List<EnemyState> enemyStates) {
        DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroService);
        List<IEnemy> enemies = enemyStates.stream()
                .map(state -> {
                    IEnemy enemy = enemyFactory.createElement(state.enemyType(), state.x(), state.y());
                    enemy.setHealth(state.health());
                    enemy.setAttackingPower(state.attackingPower());
                    return enemy;
                })
                .collect(Collectors.toList());
        enemyService.reset(enemies);
    }

    private void restoreQuest(Quest quest, QuestState questState) {
        if (quest == null) {
            return;
        }
        for (QuestObjectiveState objectiveState : questState.objectives()) {
            quest.getObjectives().stream()
                    .map(QuestObjective.class::cast)
                    .filter(obj -> obj.getDescription().equals(objectiveState.description()))
                    .findFirst()
                    .ifPresent(obj -> {
                        if (objectiveState.completed()) {
                            obj.complete();
                        }
                    });
        }
    }
}
