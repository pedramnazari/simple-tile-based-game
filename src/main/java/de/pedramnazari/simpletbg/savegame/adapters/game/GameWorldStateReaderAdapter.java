package de.pedramnazari.simpletbg.savegame.adapters.game;

import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateReader;
import de.pedramnazari.simpletbg.savegame.domain.*;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameWorldStateReaderAdapter implements ActiveGameStateReader {

    private final GameWorldService gameWorldService;
    private final QuestService questService;

    public GameWorldStateReaderAdapter(GameWorldService gameWorldService, QuestService questService) {
        this.gameWorldService = Objects.requireNonNull(gameWorldService, "gameWorldService");
        this.questService = Objects.requireNonNull(questService, "questService");
    }

    @Override
    public GameSnapshot capture() {
        GameContext context = GameContext.getInstance();
        IHero hero = context.getHero();

        HeroState heroState = new HeroState(
                hero.getX(),
                hero.getY(),
                hero.getHealth(),
                hero.getAttackingPower(),
                hero.getWeapon().map(IItem::getType).orElse(null),
                hero.getRing().map(IItem::getType).orElse(null)
        );

        InventoryState inventoryState = new InventoryState(
                hero.getInventory().getItems().stream()
                        .map(item -> new InventoryItemState(item.getType()))
                        .collect(Collectors.toList())
        );

        List<WorldItemState> worldItems = context.getItemService().getItems().stream()
                .map(item -> new WorldItemState(item.getType(), item.getX(), item.getY()))
                .collect(Collectors.toList());

        List<EnemyState> enemies = context.getEnemies().stream()
                .map(enemy -> new EnemyState(enemy.getType(), enemy.getX(), enemy.getY(), enemy.getHealth(), enemy.getAttackingPower()))
                .collect(Collectors.toList());

        Quest quest = questService.getQuest();
        List<QuestObjectiveState> objectiveStates = quest.getObjectives().stream()
                .map(obj -> (QuestObjective) obj)
                .map(obj -> new QuestObjectiveState(obj.getDescription(), obj.isCompleted()))
                .collect(Collectors.toList());

        QuestState questState = new QuestState(
                quest.getName(),
                quest.getDescription(),
                quest.isHeroMustReachExit(),
                objectiveStates
        );

        SnapshotMetadata metadata = new SnapshotMetadata(1, Instant.now());

        return new GameSnapshot(
                metadata,
                Objects.requireNonNullElse(gameWorldService.getCurrentMapIndex(), context.getCurrentMapIndex()),
                context.getCurrentMapIndex(),
                heroState,
                inventoryState,
                worldItems,
                enemies,
                questState,
                0,
                false
        );
    }
}
