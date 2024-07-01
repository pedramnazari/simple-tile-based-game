package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.config.GameInitializer;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TileMapVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(TileMapVisualizer.class.getName());

    public static final int TILE_SIZE = 80;

    private final Map<Point, ImageView> itemViews = new HashMap<>();
    private final Map<Point, ImageView> enemyViews = new HashMap<>();
    private GridPane grid;

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();

        final GameWorldController controller = GameInitializer.initAndStartGame();
        controller.setTileMapVisualizer(this);
        final Hero hero = controller.getHero();



        initFloorAndObstacleTiles(controller.getTileMap());
        initItems(controller.getItems());
        Collection<Enemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        updateEnemies(enemies);

        // add hero to grid
        final Image heroImage = new Image(getClass().getResourceAsStream("/tiles/hero/hero.png"));
        final ImageView heroView = new ImageView(heroImage);

        heroView.setFitWidth(TILE_SIZE);
        heroView.setFitHeight(TILE_SIZE);

        grid.add(heroView, hero.getX(), hero.getY());

        Scene scene = new Scene(grid, 800, 600);
        scene.setOnKeyPressed(event -> {
            MovementResult result = null;
            switch (event.getCode()) {
                case RIGHT:
                    result = controller.moveHeroToRight();
                    heroView.setX(hero.getX());
                    break;
                case LEFT:
                    result = controller.moveHeroToLeft();
                    heroView.setX(hero.getX());
                    break;
                case DOWN:
                    result = controller.moveHeroDown();
                    heroView.setY(hero.getY());
                    break;
                case UP:
                    result = controller.moveHeroUp();
                    heroView.setY(hero.getY());
                    break;
            }

            if ((result != null) && result.hasElementMoved()) {
                grid.getChildren().remove(heroView);
                grid.add(heroView, (int) heroView.getX(), (int) heroView.getY());
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void initFloorAndObstacleTiles(TileMap tileMap) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);

                Image tileImage = null;

                switch (tile.getType()) {
                    case 0:
                        tileImage = new Image(getClass().getResourceAsStream("/tiles/floor/empty.png"));
                        break;
                    case 1:
                        tileImage = new Image(getClass().getResourceAsStream("/tiles/floor/wood.png"));
                        break;
                    case 11:
                        tileImage = new Image(getClass().getResourceAsStream("/tiles/obstacles/wall.png"));
                        break;
                    default:
                        tileImage = new Image(getClass().getResourceAsStream("/tiles/floor/stone.png"));
                        break;
                }

                final ImageView imageView = new ImageView(tileImage);

                imageView.setFitWidth(TILE_SIZE);
                imageView.setFitHeight(TILE_SIZE);

                grid.add(imageView, x, y);
            }
        }
    }

    private void initItems(Collection<Item> itemMap) {
        for (Item item : itemMap) {
            final Image itemImage = new Image(getClass().getResourceAsStream("/tiles/items/yellow_key.png"));
            final ImageView itemView = new ImageView(itemImage);

            itemView.setFitWidth(TILE_SIZE);
            itemView.setFitHeight(TILE_SIZE);

            Point point = new Point(item.getX(), item.getY());
            itemViews.put(point, itemView);

            grid.add(itemView, item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<Enemy> enemies) {
        for (Point point : enemyViews.keySet()) {
            ImageView enemyRectangle = enemyViews.get(point);
            grid.getChildren().remove(enemyRectangle);
        }

        for (Enemy enemy : enemies) {
            final Image enemyImage = new Image(getClass().getResourceAsStream("/tiles/enemies/enemy.png"));
            final ImageView enemyView = new ImageView(enemyImage);

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);

            Point point = new Point(enemy.getX(), enemy.getY());
            enemyViews.put(point, enemyView);

            grid.add(enemyView, enemy.getX(), enemy.getY());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handleItemPickedUp(IItemCollector element, Item item, int itemX, int itemY) {
        Point point = new Point(itemX, itemY);
        ImageView itemView = itemViews.remove(point);

        if (itemView == null) {
            throw new IllegalArgumentException("No item rectangle found for point: " + point);
        }

        grid.getChildren().remove(itemView);
    }
}