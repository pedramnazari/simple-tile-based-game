package de.pedramnazari.simpletbg.view;

import de.pedramnazari.simpletbg.config.GameInitializer;
import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.model.*;
import de.pedramnazari.simpletbg.service.MovementResult;
import de.pedramnazari.simpletbg.service.Point;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TileMapVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(TileMapVisualizer.class.getName());

    public static final int TILE_SIZE = 80;

    private final Map<Point, Rectangle> itemRectangles = new HashMap<>();
    private final Map<Point, Rectangle> enemyRectangles = new HashMap<>();
    private GridPane grid;

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();

        final TileMapController controller = GameInitializer.initAndStartGame();
        controller.setTileMapVisualizer(this);
        final Hero hero = controller.getHero();

        initFloorAndObstacleTiles(controller.getTileMap());
        initItems(controller.getItems());
        Collection<Enemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        updateEnemies(enemies);

        // add hero to grid
        final Rectangle heroRectangle = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREEN);
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

            if ((result != null) && result.hasMoved()) {
                grid.getChildren().remove(heroRectangle);
                grid.add(heroRectangle, (int) heroRectangle.getX(), (int) heroRectangle.getY());

                if (result.isItemCollected()) {
                    Point point = new Point(result.getNewX(), result.getNewY());
                    Rectangle itemRectangle = itemRectangles.remove(point);

                    if (itemRectangle == null) {
                        throw new IllegalArgumentException("No item rectangle found for point: " + point);
                    }

                    grid.getChildren().remove(itemRectangle);
                }
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initFloorAndObstacleTiles(TileMap tileMap) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);
                Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);

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

    private void initItems(Collection<Item> itemMap) {
        for (Item item : itemMap) {
            final Rectangle itemRectangle = new Rectangle(TILE_SIZE, TILE_SIZE);
            itemRectangle.setFill(Color.YELLOW);
            Point point = new Point(item.getX(), item.getY());
            itemRectangles.put(point, itemRectangle);

            grid.add(itemRectangle, item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<Enemy> enemies) {
        for (Point point : enemyRectangles.keySet()) {
            Rectangle enemyRectangle = enemyRectangles.get(point);
            grid.getChildren().remove(enemyRectangle);
        }


        for (Enemy enemy : enemies) {
            final Rectangle enemyRectangle = new Rectangle(TILE_SIZE, TILE_SIZE);
            enemyRectangle.setFill(Color.BLUE);
            Point point = new Point(enemy.getX(), enemy.getY());
            enemyRectangles.put(point, enemyRectangle);

            grid.add(enemyRectangle, enemy.getX(), enemy.getY());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}