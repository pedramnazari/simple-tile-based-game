package de.pedramnazari.simpletbg.view;

import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.model.*;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.MovementResult;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.service.TileMapService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TileMapVisualizer extends Application {

    public static final int TILE_WIDTH = 80;
    public static final int TILE_HEIGHT = 80;

    @Override
    public void start(Stage primaryStage) {
        TileMapConfig mapConfig = AllTileMapConfigData.getMapConfig("2");

        final Inventory inventory = new Inventory();
        final Hero hero = new Hero(inventory, 1, 0);
        TileMapService tileMapService = new TileMapService(new DefaultTileFactory(), hero);
        TileMapController controller = new TileMapController(tileMapService);

        GridPane grid = new GridPane();

        // TODO: move to AllTileMapConfigData
        final TileMapConfig itemConfig = new TileMapConfig("item2", new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 100, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 100, 0, 0, 0, 100, 0, 0, 0},
        });

        initFloorAndObstacleTiles(controller.startGameUsingMap(mapConfig, itemConfig), grid);
        initItemTiles(controller.getItemMap(), grid);

        // add hero to grid
        final Rectangle heroRectangle = new Rectangle(TILE_WIDTH, TILE_HEIGHT, Color.GREEN);
        grid.add(heroRectangle, hero.getX(), hero.getY());

        Scene scene = new Scene(grid, 800, 600);
        scene.setOnKeyPressed(event -> {
            MovementResult result = null;
            switch (event.getCode()) {
                case RIGHT:
                    result = controller.moveHeroToRight();
                    heroRectangle.setX(hero.getX());

                    break;
                case LEFT:
                    result = controller.moveHeroToLeft();
                    heroRectangle.setX(hero.getX());

                    break;
                case DOWN:
                    result = controller.moveHeroDown();
                    heroRectangle.setY(hero.getY());

                    break;
                case UP:
                    result = controller.moveHeroUp();
                    heroRectangle.setY(hero.getY());

                    break;
            }

            if(result != null && result.hasMoved()) {
                grid.getChildren().remove(heroRectangle);
                grid.add(heroRectangle, (int) heroRectangle.getX(), (int) heroRectangle.getY());

                if (result.isItemCollected()) {
                    // TODO: item collected. Remove item from grid
                }
            }

        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initFloorAndObstacleTiles(TileMap tileMap, GridPane grid) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);
                Rectangle rectangle = new Rectangle(TILE_WIDTH, TILE_HEIGHT);

                switch (tile.getType()) {
                    case 0:
                        rectangle.setFill(Color.BLUE);
                        break;
                    case 1:
                        rectangle.setFill(Color.GRAY);
                        break;
                    case 11:
                        rectangle.setFill(Color.RED);
                        break;
                    default:
                        rectangle.setFill(Color.BLACK);
                        break;
                }

                grid.add(rectangle, x, y);
            }
        }
    }

    private void initItemTiles(TileMap itemMap, GridPane grid) {
        for (int y = 0; y < itemMap.getHeight(); y++) {
            for (int x = 0; x < itemMap.getWidth(); x++) {
                Tile tile = itemMap.getTile(x, y);
                if (tile.getType() == 0) {
                    continue;
                }
                Rectangle itemRectangle = new Rectangle(TILE_WIDTH, TILE_HEIGHT);

                // Set color based on tile type
                switch (tile.getType()) {
                    case 100:
                        itemRectangle.setFill(Color.YELLOW);
                        break;
                    default:
                        break;
                }

                grid.add(itemRectangle, x, y);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}