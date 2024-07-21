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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: One big class, split into smaller classes
public class GameWorldVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(GameWorldVisualizer.class.getName());

    public static final int TILE_SIZE = 48;

    private final Map<Point, ItemView> itemViews = new HashMap<>();
    private final Map<IEnemy, EnemyView> enemyViews = new HashMap<>();
    private final Collection<BombView> bombsViews = new ArrayList<>();
    private GridPane tilesGrid;
    private GridPane itemsGrid;
    private GridPane bombsGrid;
    private GridPane charactersGrid;
    private Scene scene;
    private HeroView heroView;
    private TilePane inventory;

    @Override
    public void start(Stage primaryStage) {


        final GameWorldController controller = GameInitializer.initAndStartGame();
        controller.setTileMapVisualizer(this);
        final IHero hero = controller.getHero();


        final StackPane stackPane = new StackPane();
        tilesGrid = createGridPane(controller.getTileMap());
        itemsGrid = createGridPane(controller.getTileMap());
        bombsGrid = createGridPane(controller.getTileMap());
        charactersGrid = createGridPane(controller.getTileMap());

        initFloorAndObstacleTiles(controller.getTileMap());


        updateItems(controller.getItems());
        Collection<IEnemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        updateEnemies(enemies);

        // add hero to grid
        final Image heroImage = new Image(getClass().getResourceAsStream("/tiles/hero/hero.png"));
        heroView = new HeroView(hero, heroImage, TILE_SIZE);

        charactersGrid.add(heroView.getImageView(), hero.getX(), hero.getY());

        stackPane.getChildren().addAll(tilesGrid, itemsGrid, bombsGrid, charactersGrid);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(stackPane);

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
                logger.info("Control key pressed");
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
                charactersGrid.getChildren().remove(heroView.getImageView());
                charactersGrid.add(heroView.getImageView(), heroView.getX(), heroView.getY());
                heroView.getImageView().setOpacity(1.0);
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane(final TileMap tileMap) {
        GridPane gridPane = new GridPane();
        for (int y = 0; y < tileMap.getHeight(); y++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMinHeight(TILE_SIZE);
            rowConst.setMaxHeight(TILE_SIZE);
            rowConst.setPrefHeight(TILE_SIZE);
            gridPane.getRowConstraints().add(rowConst);
        }

        for (int x = 0; x < tileMap.getWidth(); x++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMinWidth(TILE_SIZE);
            colConst.setMaxWidth(TILE_SIZE);
            colConst.setPrefWidth(TILE_SIZE);
            gridPane.getColumnConstraints().add(colConst);

        }
        return gridPane;
    }


    private void initFloorAndObstacleTiles(TileMap tileMap) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);

                String imagePath = getImagePath(tile);

                Image tileImage = new Image(getClass().getResourceAsStream(imagePath));
                final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);

                tilesGrid.add(tileView.getImageView(), x, y);
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
            itemsGrid.getChildren().remove(itemView.getImageView());
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

            itemsGrid.add(itemView.getImageView(), item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<IEnemy> enemies) {
        // TODO: do not delete views, instead update them
        for (IEnemy enemy : enemyViews.keySet()) {
            EnemyView enemyView = enemyViews.get(enemy);
            charactersGrid.getChildren().remove(enemyView.getImageView());
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

            if (enemyView == null) {
                String imagePath = getImagePath(enemy);
                final Image enemyImage = new Image(getClass().getResourceAsStream(imagePath));
                enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);
            }


            enemyViews.put(enemy, enemyView);
            charactersGrid.add(enemyView.getImageView(), enemy.getX(), enemy.getY());
        }
    }

    private String getImagePath(IEnemy enemy) {
        String imagePath;

        if (enemy.getType() == TileType.ENEMY_LR.getType()) {
            imagePath = "/tiles/enemies/enemy.png";
        }
        else if (enemy.getType() == TileType.ENEMY_TD.getType()) {
            imagePath = "/tiles/enemies/enemy2.png";
        }
        else if (enemy.getType() == TileType.ENEMY_2D.getType()) {
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
        EnemyView enemyView = enemyViews.get(enemy);

        if ((enemyView == null)) {
            throw new IllegalArgumentException("No enemy rectangle found for enemy at position: " + enemy.getX() + ", " + enemy.getY());
        }

        if (enemy.getHealth() > 0) {
            enemyView.getImageView().setOpacity(0.5);
        }
        else {
            boolean deleted = charactersGrid.getChildren().remove(enemyView.getImageView());

            if (!deleted) {
                throw new IllegalArgumentException("Enemy rectangle not found in grid");
            }

            enemyViews.remove(enemy);
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

        itemsGrid.getChildren().remove(itemView.getImageView());
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
        logger.info("Remove bomb: " + bomb);
        BombView bombView = null;

        for (BombView view : bombsViews) {
            if (view.getTileMapElement().equals(bomb)) {
                if (view.isExplosion()) {
                    throw new IllegalArgumentException("Bomb rectangle is an explosion: " + view);
                }

                if (bombView == null) {
                    bombView = view;
                }
                else {
                    throw new IllegalArgumentException("Multiple bomb rectangles found for bomb: " + bomb);
                }
            }
        }

        if (bombView == null) {
            throw new IllegalArgumentException("No bomb rectangle found for bomb: " + bomb);
        }

        bombsViews.remove(bombView);
        bombsGrid.getChildren().remove(bombView.getImageView());
    }

    public void bombExploded(Bomb bomb, List<Point> explosionPoints) {
        logger.info("Bomb exploded: " + bomb);
        removeBomb(bomb);

        addExplosionForBomb(bomb, explosionPoints);
    }

    private void addExplosionForBomb(Bomb bomb, List<Point> explosionPoints) {
        final Image attackImage = new Image(getClass().getResourceAsStream("/tiles/items/weapons/bomb_explosion.png"));
        for (Point explosionPoint : explosionPoints) {
            final BombView explosionView = new BombView(bomb, attackImage, true, TILE_SIZE);
            bombsViews.add(explosionView);
            bombsGrid.add(explosionView.getImageView(), explosionPoint.getX(), explosionPoint.getY());
        }
    }

    public void bombExplosionFinished(Bomb bomb) {
        logger.info("Bomb explosion finished: " + bomb);

        // Remove all explosions that belong to the bomb
        final List<BombView> explosionsToRemove = new ArrayList<>();

        for (BombView bombView : bombsViews) {
            if (bombView.getTileMapElement().equals(bomb)) {
                if (!bombView.isExplosion()) {
                    throw new IllegalArgumentException("Bomb rectangle is not an explosion: " + bombView);
                }
                bombsGrid.getChildren().remove(bombView.getImageView());
                explosionsToRemove.add(bombView);
            }
        }

        bombsViews.removeAll(explosionsToRemove);
    }

    public synchronized void updateBombs(Collection<Bomb> bombs) {
        logger.info("Update bombs");

        // TODO: do not delete views, instead update them
        final Collection<BombView> bombViewsToRemove = new ArrayList<>();

        for (BombView bombView : bombsViews) {
            if (!bombView.isExplosion()) {
                bombsGrid.getChildren().remove(bombView.getImageView());
                bombViewsToRemove.add(bombView);
            }
        }

        bombsViews.removeAll(bombViewsToRemove);

        for (Bomb bomb : bombs) {
            if (!bomb.isExplosionOngoing()) {
                for (BombView bombView : bombsViews) {
                    if (bombView.getTileMapElement().equals(bomb)) {
                        throw new IllegalArgumentException("Bomb rectangle already exists for bomb: " + bomb);
                    }
                }
            }
            else {
                // Skip bombs that are currently exploding
                continue;
            }

            String imagePath = switch (bomb.getType()) {
                case 231 -> "/tiles/items/weapons/bomb.png";
                default -> throw new IllegalArgumentException("Unknown bomb type: " + bomb.getType());
            };

            final Image bombImage = new Image(getClass().getResourceAsStream(imagePath));
            final BombView bombView = new BombView(bomb, bombImage, false, TILE_SIZE);

            bombsViews.add(bombView);
            bombsGrid.add(bombView.getImageView(), bomb.getX(), bomb.getY());
        }

    }
}