package de.pedramnazari.simpletbg.ui.controller;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyObserver;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.view.GameWorldVisualizer;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

public class GameWorldController implements IEnemyObserver, IItemPickUpListener, IEnemyHitListener {

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

    public void startGameUsingMap(final Tile[][] tiles, Collection<Item> items, Collection<Enemy> enemiesConfig, int heroX, int heroY) {
        gameWorldService.createAndInitMap(tiles, items, enemiesConfig, heroX, heroY);
        gameWorldService.getHeroService().addItemPickupListener(this);
        gameWorldService.getEnemyService().addItemPickupListener(this);
        gameWorldService.getEnemyService().addEnemyHitListener(this);

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

    public Collection<Item> getItems() {
        return gameWorldService.getItemService().getItems();
    }


    public TileMap getTileMap() {
        return gameWorldService.getTileMap();
    }

    public Hero getHero() {
        return gameWorldService.getHero();
    }

    public Collection<Enemy> getEnemies() {
        return gameWorldService.getEnemies();
    }

    @Override
    public void update(Collection<Enemy> enemies) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateEnemies(enemies));
    }

    @Override
    public void onItemPickedUp(IItemCollector element, Item item) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.handleItemPickedUp(element, item));
    }

    public void heroAttacks() {
        gameWorldService.heroAttacks();
    }

    @Override
    public void onEnemyHit(Enemy enemy, int damage) {
        gameWorldVisualizer.updateEnemy(enemy, damage);
    }

    @Override
    public void onEnemyDefeated(Enemy enemy) {
        // already handled in onEnemyHit
    }

    @Override
    public void onAllEnemiesDefeated() {
        gameWorldVisualizer.handleAllEnemiesDefeated();
    }
}
