package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.config.GameInitializer;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWorldVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(GameWorldVisualizer.class.getName());

    public static final int TILE_SIZE = 80;

    private final Map<Point, ItemView> itemViews = new HashMap<>();
    private final Map<Point, EnemyView> enemyViews = new HashMap<>();
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
        final HeroView heroView = new HeroView(hero, heroImage, TILE_SIZE);

        grid.add(heroView.getImageView(), hero.getX(), hero.getY());

        Scene scene = new Scene(grid, 800, 600);
        scene.setOnKeyPressed(event -> {
            MovementResult result = null;

            if (event.isControlDown()) {
                controller.heroAttacks();
            }

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
                grid.getChildren().remove(heroView.getImageView());
                grid.add(heroView.getImageView(), (int) heroView.getX(), (int) heroView.getY());
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void initFloorAndObstacleTiles(TileMap tileMap) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);


                String imagePath = "";

                if (tile.getType() == TileType.WOOD.getType()) {
                    imagePath = "/tiles/floor/wood.png";
                }
                else if (tile.getType() == TileType.STONE.getType()) {
                        imagePath = "/tiles/floor/stone.png";
                }
                else if (tile.getType() == TileType.WALL.getType()) {
                    imagePath = "/tiles/obstacles/wall.png";
                }
                else if (tile.getType() == TileType.EMPTY.getType()) {
                        imagePath = "/tiles/floor/empty.png";
                }

                Image tileImage = new Image(getClass().getResourceAsStream(imagePath));
                final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);

                grid.add(tileView.getImageView(), x, y);
            }
        }
    }

    private void initItems(Collection<Item> itemMap) {
        for (Item item : itemMap) {

            String imagePath;

            switch (item.getType()) {
                case 100:
                    imagePath = "/tiles/items/yellow_key.png";
                    break;
                case 101:
                    imagePath = "/tiles/items/yellow_key_stone.png";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown item type: " + item.getType());
            }

            final Image itemImage = new Image(getClass().getResourceAsStream(imagePath));
            final ItemView itemView = new ItemView(item, itemImage, TILE_SIZE);

            Point point = new Point(item.getX(), item.getY());
            itemViews.put(point, itemView);

            grid.add(itemView.getImageView(), item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<Enemy> enemies) {
        // TODO: do not delete views, instead update them
        for (Point point : enemyViews.keySet()) {
            EnemyView enemyView = enemyViews.get(point);
            grid.getChildren().remove(enemyView.getImageView());
        }

        for (Enemy enemy : enemies) {
            String imagePath = "";

            if (enemy.getType() == TileType.ENEMY_LR.getType()) {
                imagePath = "/tiles/enemies/enemy.png";
            } else if (enemy.getType() == TileType.ENEMY_TD.getType()) {
                imagePath = "/tiles/enemies/enemy2.png";
            } else if (enemy.getType() == TileType.ENEMY_2D.getType()) {
                imagePath = "/tiles/enemies/enemy3.png";
            }
            else {
                throw new IllegalArgumentException("Unknown enemy type: " + enemy.getType());
            }

            final Image enemyImage = new Image(getClass().getResourceAsStream(imagePath));
            final EnemyView enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);

            Point point = new Point(enemy.getX(), enemy.getY());
            enemyViews.put(point, enemyView);

            grid.add(enemyView.getImageView(), enemy.getX(), enemy.getY());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handleItemPickedUp(IItemCollector element, Item item, int itemX, int itemY) {
        Point point = new Point(itemX, itemY);
        ItemView itemView = itemViews.remove(point);

        if (itemView == null) {
            throw new IllegalArgumentException("No item rectangle found for point: " + point);
        }

        grid.getChildren().remove(itemView.getImageView());
    }

    public void updateEnemy(Enemy enemy, int damage) {
        Point point = new Point(enemy.getX(), enemy.getY());
        EnemyView enemyView = enemyViews.get(point);

        if (enemyView == null) {
            throw new IllegalArgumentException("No enemy rectangle found for point: " + point);
        }

        grid.getChildren().remove(enemyView.getImageView());
        enemyViews.remove(point);
    }
}