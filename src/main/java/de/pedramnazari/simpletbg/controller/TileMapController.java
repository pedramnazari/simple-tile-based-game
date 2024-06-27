package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.Item;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.IEnemyObserver;
import de.pedramnazari.simpletbg.service.MovementResult;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.TileMapService;
import de.pedramnazari.simpletbg.view.TileMapVisualizer;
import javafx.application.Platform;

import java.util.Collection;

public class TileMapController implements IEnemyObserver {

    private final TileMapService tileMapService;
    // TODO: remove this dependency
    private TileMapVisualizer tileMapVisualizer;

    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public void setTileMapVisualizer(TileMapVisualizer tileMapVisualizer) {
        this.tileMapVisualizer = tileMapVisualizer;
    }

    @Deprecated
    public TileMap startNewGame() {
        return this.startGameUsingMap(AllTileMapConfigData.getMapConfig("1"), 1, 0);
    }

    public TileMap startGameUsingMap(TileMapConfig mapConfig, int heroX, int heroY) {
        return tileMapService.createAndInitMap(mapConfig, heroX, heroY);
    }

    public void startGameUsingMap(TileMapConfig mapConfig, TileMapConfig itemConfig, TileMapConfig enemiesConfig, int heroX, int heroY) {
        tileMapService.createAndInitMap(mapConfig, itemConfig, enemiesConfig, heroX, heroY);
        tileMapService.start();
    }

    public MovementResult moveHeroToRight() {
        return tileMapService.moveHeroToRight();
    }

    public MovementResult moveHeroToLeft() {
        return tileMapService.moveHeroToLeft();
    }

    public MovementResult moveHeroUp() {
        return tileMapService.moveHeroUp();
    }

    public MovementResult moveHeroDown() {
        return tileMapService.moveHeroDown();
    }

    public Collection<Item> getItems() {
        return tileMapService.getItems();
    }


    public TileMap getTileMap() {
        return tileMapService.getTileMap();
    }

    public Hero getHero() {
        return tileMapService.getHero();
    }

    public Collection<Enemy> getEnemies() {
        return tileMapService.getEnemies();
    }

    @Override
    public void update(Collection<Enemy> enemies) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> tileMapVisualizer.updateEnemies(enemies));
    }
}
