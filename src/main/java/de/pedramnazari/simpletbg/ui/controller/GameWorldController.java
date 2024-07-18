package de.pedramnazari.simpletbg.ui.controller;

import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyObserver;
import de.pedramnazari.simpletbg.character.service.IHeroHitListener;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.Bomb;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.view.GameWorldVisualizer;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class GameWorldController implements IEnemyObserver, IItemPickUpListener, IEnemyHitListener, IHeroHitListener {

    private static final Logger logger = Logger.getLogger(GameWorldController.class.getName());

    private final GameWorldService gameWorldService;
    // TODO: remove this dependency
    private GameWorldVisualizer gameWorldVisualizer;

    public GameWorldController(final GameWorldService gameWorldService) {
        this.gameWorldService = gameWorldService;
    }

    public void setTileMapVisualizer(GameWorldVisualizer gameWorldVisualizer) {
        this.gameWorldVisualizer = gameWorldVisualizer;
    }

    public TileMap startGameUsingMap(Tile[][] tiles, int heroX, int heroY) {
        return gameWorldService.createAndInitMap(tiles, List.of(), List.of(), heroX, heroY);
    }

    public void startGameUsingMap(final Tile[][] tiles, Collection<IItem> items, Collection<IEnemy> enemiesConfig, int heroX, int heroY) {
        gameWorldService.createAndInitMap(tiles, items, enemiesConfig, heroX, heroY);

        GameContext.initialize(gameWorldService.getTileMap(), gameWorldService.getItemService(), gameWorldService.getHeroService(), gameWorldService.getEnemyService(), "0");

        gameWorldService.start();
    }

    public MovementResult moveHeroToRight() {
        return gameWorldService.moveHeroToRight();
    }

    public MovementResult moveHeroToLeft() {
        return gameWorldService.moveHeroToLeft();
    }

    public MovementResult moveHeroUp() {
        return gameWorldService.moveHeroUp();
    }

    public MovementResult moveHeroDown() {
        return gameWorldService.moveHeroDown();
    }

    public Collection<IItem> getItems() {
        return gameWorldService.getItemService().getItems();
    }


    public TileMap getTileMap() {
        return gameWorldService.getTileMap();
    }

    public IHero getHero() {
        return gameWorldService.getHero();
    }

    public Collection<IEnemy> getEnemies() {
        return gameWorldService.getEnemies();
    }

    @Override
    public void update(Collection<IEnemy> enemies) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateEnemies(enemies));
    }

    @Override
    public void onItemPickedUp(ICharacter element, IItem item) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.handleItemPickedUp(element, item));
    }

    public void heroAttacks() {
        gameWorldService.heroAttacks();
    }

    @Override
    public void onEnemyHit(IEnemy enemy, int damage) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateEnemy(enemy, damage));
    }

    @Override
    public void onEnemyDefeated(IEnemy enemy) {
        // already handled in onEnemyHit
    }

    @Override
    public void onAllEnemiesDefeated() {
        gameWorldVisualizer.handleAllEnemiesDefeated();
    }

    @Override
    public void onHeroHit(IHero hero, ICharacter attackingCharacter, int damage) {
        if (hero.getHealth() > 0) {
            gameWorldVisualizer.handleHeroHit();
        }
        else {
            gameWorldVisualizer.handleHeroDefeated();
        }
    }

    public void updateItems() {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateItems(getItems()));
    }

    public void bombExploded(Bomb bomb, List<Point> attackPoints) {
        // GUI operations must be executed on the JavaFX application thread
        logger.info("Bomb exploded" + bomb);
        Platform.runLater(() -> gameWorldVisualizer.bombExploded(bomb, attackPoints));
    }

    public void bombExplosionFinished(Bomb bomb) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.bombExplosionFinished(bomb));
    }

    public void updateBombs(Collection<Bomb> bombs) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateBombs(bombs));
    }
}
