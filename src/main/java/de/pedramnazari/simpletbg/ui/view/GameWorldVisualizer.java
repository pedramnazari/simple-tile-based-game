package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.config.GameInitializer;
import de.pedramnazari.simpletbg.inventory.model.Bomb;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: One big class, split into smaller classes
public class GameWorldVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(GameWorldVisualizer.class.getName());

    public static final int TILE_SIZE = 48;

    private final Map<Point, ItemView> itemViews = new HashMap<>();
    private final Map<Point, EnemyView> enemyViews = new HashMap<>();
    private final Map<Point, BombView> bombViews = new HashMap<>();
    private GridPane grid;
    private Scene scene;
    private HeroView heroView;
    private TilePane inventory;

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();

        final GameWorldController controller = GameInitializer.initAndStartGame();
        controller.setTileMapVisualizer(this);
        final IHero hero = controller.getHero();


        initFloorAndObstacleTiles(controller.getTileMap());
        updateItems(controller.getItems());
        Collection<IEnemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        updateEnemies(enemies);

        // add hero to grid
        final Image heroImage = new Image(getClass().getResourceAsStream("/tiles/hero/hero.png"));
        heroView = new HeroView(hero, heroImage, TILE_SIZE);

        grid.add(heroView.getImageView(), hero.getX(), hero.getY());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);

        inventory = new TilePane();
        inventory.setPrefTileWidth(TILE_SIZE);
        inventory.setPrefTileHeight(TILE_SIZE);
        inventory.setAlignment(Pos.TOP_LEFT);
        borderPane.setBottom(inventory);


        scene = new Scene(borderPane, 1100, 575);
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
                heroView.getImageView().setOpacity(1.0);
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

    public void updateItems(final Collection<IItem> items) {
        // TODO: do not delete views, instead update them
        for (Point point : itemViews.keySet()) {
            ItemView itemView = itemViews.get(point);
            grid.getChildren().remove(itemView.getImageView());
        }

        for (IItem item : items) {

            String imagePath = switch (item.getType()) {
                case 100 -> "/tiles/items/yellow_key.png";
                case 101 -> "/tiles/items/yellow_key_stone.png";
                case 200 -> "/tiles/items/weapons/sword.png";
                case 201 -> "/tiles/items/weapons/sword2.png";
                case 220 -> "/tiles/items/weapons/lance.png";
                case 221 -> "/tiles/items/weapons/double_ended_lance.png";
                case 222 -> "/tiles/items/weapons/multi_spike_lance.png";
                case 230 -> "/tiles/items/weapons/bomb_placer.png";
                case 300 -> "/tiles/items/rings/magic_ring1.png";
                default -> throw new IllegalArgumentException("Unknown item type: " + item.getType());
            };

            final Image itemImage = new Image(getClass().getResourceAsStream(imagePath));
            final ItemView itemView = new ItemView(item, itemImage, TILE_SIZE);

            Point point = new Point(item.getX(), item.getY());
            itemViews.put(point, itemView);

            grid.add(itemView.getImageView(), item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<IEnemy> enemies) {
        // TODO: do not delete views, instead update them
        for (Point point : enemyViews.keySet()) {
            EnemyView enemyView = enemyViews.get(point);
            grid.getChildren().remove(enemyView.getImageView());
        }

        for (IEnemy enemy : enemies) {
            EnemyView enemyView = null;
            for (EnemyView oldView : enemyViews.values()) {
                if(oldView.getTileMapElement().equals(enemy)) {
                    enemyView = oldView;
                    enemyView.getImageView().setOpacity(1.0);
                    break;
                }
            }

            if(enemyView == null) {
                String imagePath = getImagePath(enemy);
                final Image enemyImage = new Image(getClass().getResourceAsStream(imagePath));
                enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);
            }


            Point point = new Point(enemy.getX(), enemy.getY());
            enemyViews.put(point, enemyView);

            grid.add(enemyView.getImageView(), enemy.getX(), enemy.getY());
        }
    }

    private String getImagePath(IEnemy enemy) {
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


    public void updateEnemy(IEnemy enemy, int damage) {
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
    }

    public void handleItemPickedUp(ICharacter element, IItem item) {
        ItemView itemView = removeItem(item);

        if (element instanceof IHero hero) {
            inventory.getChildren().add(itemView.getImageView());
        }

    }

    private ItemView removeItem(IItem item) {
        Point point = new Point(item.getX(), item.getY());
        ItemView itemView = itemViews.remove(point);

        if (itemView == null) {
            throw new IllegalArgumentException("No item rectangle found for point: " + point);
        }

        grid.getChildren().remove(itemView.getImageView());
        return itemView;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handleHeroDefeated() {
        logger.log(Level.INFO, "Hero defeated! -> Stop Game");
        heroView.getImageView().setOpacity(0.5);
        scene.setOnKeyPressed(null);
    }

    public void handleHeroHit() {
        heroView.getImageView().setOpacity(0.5);
    }

    private void removeBomb(Bomb bomb) {
        Point point = new Point(bomb.getX(), bomb.getY());
        BombView bombView = bombViews.remove(point);

        if (bombView == null) {
            throw new IllegalArgumentException("No bomb rectangle found for point: " + point);
        }

        grid.getChildren().remove(bombView.getImageView());
    }

    public void bombExploded(Bomb bomb, List<Point> explosionPoints) {
        removeBomb(bomb);

        final Image attackImage = new Image(getClass().getResourceAsStream("/tiles/items/weapons/bomb_explosion.png"));
        for (Point explosionPoint : explosionPoints) {
            final BombView explosionView = new BombView(bomb, attackImage, TILE_SIZE);
            bombViews.put(explosionPoint, explosionView);
            grid.add(explosionView.getImageView(), explosionPoint.getX(), explosionPoint.getY());
        }
    }

    public void bombExplosionFinished(Bomb bomb) {
        // Remove all explosions that belong to the bomb
        for (BombView bombView : bombViews.values()) {
            if (bombView.getTileMapElement().equals(bomb)) {
                grid.getChildren().remove(bombView.getImageView());
            }
        }
    }

    public void updateBombs(Collection<Bomb> bombs) {
        // TODO: do not delete views, instead update them
        for (Point point : bombViews.keySet()) {
            BombView bombView = bombViews.get(point);
            grid.getChildren().remove(bombView.getImageView());
        }

        for (Bomb bomb : bombs) {

            String imagePath = switch (bomb.getType()) {
                case 231 -> "/tiles/items/weapons/bomb.png";
                default -> throw new IllegalArgumentException("Unknown bomb type: " + bomb.getType());
            };

            final Image bombImage = new Image(getClass().getResourceAsStream(imagePath));
            final BombView bombView = new BombView(bomb, bombImage, TILE_SIZE);

            Point point = new Point(bomb.getX(), bomb.getY());
            bombViews.put(point, bombView);

            grid.add(bombView.getImageView(), bomb.getX(), bomb.getY());
        }

    }
}