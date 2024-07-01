package de.pedramnazari.simpletbg.ui.controller;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyObserver;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.view.TileMapVisualizer;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

public class GameWorldController implements IEnemyObserver, IItemPickUpListener {

    private final GameWorldService gameWorldService;
    // TODO: remove this dependency
    private TileMapVisualizer tileMapVisualizer;

    public GameWorldController(final GameWorldService gameWorldService) {
        this.gameWorldService = gameWorldService;
    }

    public void setTileMapVisualizer(TileMapVisualizer tileMapVisualizer) {
        this.tileMapVisualizer = tileMapVisualizer;
    }

    public TileMap startGameUsingMap(Tile[][] tiles, int heroX, int heroY) {
        return gameWorldService.createAndInitMap(tiles, List.of(), List.of(), heroX, heroY);
    }

    public void startGameUsingMap(final Tile[][] tiles, Collection<Item> items, Collection<Enemy> enemiesConfig, int heroX, int heroY) {
        gameWorldService.createAndInitMap(tiles, items, enemiesConfig, heroX, heroY);
        gameWorldService.getHeroMovementService().addItemPickupListener(this);
        gameWorldService.getEnemyMovementService().addItemPickupListener(this);

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
        return gameWorldService.getItems();
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
        Platform.runLater(() -> tileMapVisualizer.updateEnemies(enemies));
    }

    @Override
    public void onItemPickedUp(IItemCollector element, Item item, int itemX, int itemY) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> tileMapVisualizer.handleItemPickedUp(element, item, itemX, itemY));
    }
}
