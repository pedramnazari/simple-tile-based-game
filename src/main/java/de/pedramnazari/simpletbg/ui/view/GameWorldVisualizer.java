package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.config.GameInitializer;
import de.pedramnazari.simpletbg.tilemap.model.*;
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
    private Scene scene;
    private HeroView heroView;

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();

        final GameWorldController controller = GameInitializer.initAndStartGame();
        controller.setTileMapVisualizer(this);
        final IHero hero = controller.getHero();


        initFloorAndObstacleTiles(controller.getTileMap());
        initItems(controller.getItems());
        Collection<Enemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        updateEnemies(enemies);

        // add hero to grid
        final Image heroImage = new Image(getClass().getResourceAsStream("/tiles/hero/hero.png"));
        heroView = new HeroView(hero, heroImage, TILE_SIZE);

        grid.add(heroView.getImageView(), hero.getX(), hero.getY());

        scene = new Scene(grid, 1200, 700);
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
                grid.add(heroView.getImageView(), heroView.getX(), heroView.getY());
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void initFloorAndObstacleTiles(TileMap tileMap) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);

                String imagePath = getImagePath(tile);

                Image tileImage = new Image(getClass().getResourceAsStream(imagePath));
                final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);

                grid.add(tileView.getImageView(), x, y);
            }
        }
    }

    private String getImagePath(Tile tile) {
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
        else if (tile.getType() == TileType.GRASS.getType()) {
            imagePath = "/tiles/floor/grass.png";
        }
        else if (tile.getType() == TileType.FLOOR.getType()) {
            imagePath = "/tiles/floor/floor.png";
        }
        else if (tile.getType() == TileType.EMPTY.getType()) {
                imagePath = "/tiles/floor/empty.png";
        }
        return imagePath;
    }

    private void initItems(Collection<IItem> itemMap) {
        for (IItem item : itemMap) {

            String imagePath = switch (item.getType()) {
                case 100 -> "/tiles/items/yellow_key.png";
                case 101 -> "/tiles/items/yellow_key_stone.png";
                case 200 -> "/tiles/items/weapons/sword.png";
                case 201 -> "/tiles/items/weapons/sword2.png";
                default -> throw new IllegalArgumentException("Unknown item type: " + item.getType());
            };

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
            String imagePath = getImagePath(enemy);

            final Image enemyImage = new Image(getClass().getResourceAsStream(imagePath));
            final EnemyView enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);

            Point point = new Point(enemy.getX(), enemy.getY());
            enemyViews.put(point, enemyView);

            grid.add(enemyView.getImageView(), enemy.getX(), enemy.getY());
        }
    }

    private String getImagePath(Enemy enemy) {
        String imagePath;

        if (enemy.getType() == TileType.ENEMY_LR.getType()) {
            imagePath = "/tiles/enemies/enemy.png";
        } else if (enemy.getType() == TileType.ENEMY_TD.getType()) {
            imagePath = "/tiles/enemies/enemy2.png";
        } else if (enemy.getType() == TileType.ENEMY_2D.getType()) {
            imagePath = "/tiles/enemies/enemy3.png";
        }
        else if (enemy.getType() == TileType.ENEMY_FH.getType()) {
            imagePath = "/tiles/enemies/enemy4.png";
        }
        else {
            throw new IllegalArgumentException("Unknown enemy type: " + enemy.getType());
        }
        return imagePath;
    }


    public void updateEnemy(Enemy enemy, int damage) {
        Point point = new Point(enemy.getX(), enemy.getY());
        EnemyView enemyView = enemyViews.get(point);

        if (enemyView == null) {
            throw new IllegalArgumentException("No enemy rectangle found for point: " + point);
        }

        if (enemy.getHealth() > 0) {
            enemyView.getImageView().setOpacity(0.5);
        }
        else {
            grid.getChildren().remove(enemyView.getImageView());
            enemyViews.remove(point);
        }
    }

    public void handleAllEnemiesDefeated() {
        logger.log(Level.INFO, "All enemies defeated! -> Stop Game");
        scene.setOnKeyPressed(null);
    }

    public void handleItemPickedUp(ICharacter element, IItem item) {
        Point point = new Point(item.getX(), item.getY());
        ItemView itemView = itemViews.remove(point);

        if (itemView == null) {
            throw new IllegalArgumentException("No item rectangle found for point: " + point);
        }

        grid.getChildren().remove(itemView.getImageView());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handleHeroDefeated() {
        logger.log(Level.INFO, "Hero defeated! -> Stop Game");
        heroView.getImageView().setOpacity(0.5);
        scene.setOnKeyPressed(null);
    }
}