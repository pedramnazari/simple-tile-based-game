package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.drivers.GameApplication;
import de.pedramnazari.simpletbg.drivers.GameSession;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

// TODO: One big class, split into smaller classes
public class GameWorldVisualizer extends Application {

    private static final Logger logger = Logger.getLogger(GameWorldVisualizer.class.getName());

    public static final int TILE_SIZE = 48;
    private static final Duration HERO_MOVE_ANIMATION_DURATION = Duration.millis(90);
    private static final Duration ENEMY_MOVE_ANIMATION_DURATION = Duration.millis(1000);
    private static final Duration PROJECTILE_MOVE_ANIMATION_DURATION = Duration.millis(90);
    private static final int VISIBLE_COLUMNS = 15;
    private static final int VISIBLE_ROWS = 11;

    private final Map<Point, ItemView> itemViews = new HashMap<>();
    private final Map<IEnemy, EnemyView> enemyViews = new HashMap<>();
    private final Collection<BombView> bombsViews = new ArrayList<>();
    private final Map<IProjectile, ProjectileView> projectileViews = new HashMap<>();
    private GridPane tilesGrid;
    private GridPane itemsGrid;
    private GridPane bombsGrid;
    private GridPane projectilesGrid;
    private GridPane charactersGrid;
    private Scene scene;
    private HeroView heroView;
    private TilePane inventory;
    private final GameApplication gameApplication;
    private GameSession session;
    private GameWorldController controller;
    private IHero hero;
    private StackPane stackPane;
    private Pane cameraViewport;
    private Stage primaryStage;
    private double viewportWidth;
    private double viewportHeight;
    private double mapPixelWidth;
    private double mapPixelHeight;

    private boolean right, left, down, up;
    private GameMapDefinition mapDefinition;
    private Map<Point, TileView> tilesView = new HashMap<>();
    private Label enemyCountLabel;
    private int currentEnemyCount;
    private final TileMapElementAnimator heroAnimator = new TileMapElementAnimator(HERO_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private final TileMapElementAnimator enemyAnimator = new TileMapElementAnimator(ENEMY_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private final TileMapElementAnimator projectileAnimator = new TileMapElementAnimator(PROJECTILE_MOVE_ANIMATION_DURATION, TILE_SIZE);

    public GameWorldVisualizer(GameApplication gameApplication) {
        this.gameApplication = Objects.requireNonNull(gameApplication, "gameApplication");
    }

    public GameWorldVisualizer() {
        this(new GameApplication());
    }

    public void setSession(GameSession session) {
        this.session = Objects.requireNonNull(session, "session");
        this.mapDefinition = session.mapDefinition();
    }

    @Override
    public void start(Stage primaryStage) {
        if (session == null) {
            throw new IllegalStateException("Game session must be set before starting the visualizer");
        }
        this.primaryStage = primaryStage;
        controller = session.controller();
        this.mapDefinition = session.mapDefinition();
        tilesView = new HashMap<>();
        controller.setTileMapVisualizer(this);

        final TileMap tileMap = controller.getTileMap();
        hero = controller.getHero();

        stackPane = new StackPane();
        mapPixelWidth = tileMap.getWidth() * TILE_SIZE;
        mapPixelHeight = tileMap.getHeight() * TILE_SIZE;
        stackPane.setPrefSize(mapPixelWidth, mapPixelHeight);

        viewportWidth = VISIBLE_COLUMNS * TILE_SIZE;
        viewportHeight = VISIBLE_ROWS * TILE_SIZE;

        cameraViewport = new Pane(stackPane);
        cameraViewport.setPrefSize(viewportWidth, viewportHeight);
        cameraViewport.setMinSize(viewportWidth, viewportHeight);
        cameraViewport.setMaxSize(viewportWidth, viewportHeight);

        Rectangle clip = new Rectangle(viewportWidth, viewportHeight);
        clip.widthProperty().bind(cameraViewport.widthProperty());
        clip.heightProperty().bind(cameraViewport.heightProperty());
        cameraViewport.setClip(clip);

        tilesGrid = createGridPane(tileMap);
        itemsGrid = createGridPane(tileMap);
        bombsGrid = createGridPane(tileMap);
        projectilesGrid = createGridPane(tileMap);
        charactersGrid = createGridPane(tileMap);

        initFloorAndObstacleTiles(controller.getTileMap());

        updateItems(controller.getItems());
        Collection<IEnemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        currentEnemyCount = enemies.size();
        updateEnemies(enemies);
        refreshInventory();

        // add hero to grid
        final Image heroImage = new Image(requireNonNull(getClass().getResourceAsStream("/tiles/hero/hero.png")));
        heroView = new HeroView(hero, heroImage, TILE_SIZE);

        charactersGrid.add(heroView.getDisplayNode(), hero.getX(), hero.getY());

        updateHeroHealthView();

        stackPane.getChildren().addAll(tilesGrid, itemsGrid, bombsGrid, projectilesGrid, charactersGrid);

        BorderPane borderPane = new BorderPane();
        StackPane viewportContainer = new StackPane(cameraViewport);
        viewportContainer.setAlignment(Pos.CENTER);
        borderPane.setCenter(viewportContainer);

        inventory = new TilePane();
        inventory.setPrefTileWidth(TILE_SIZE);
        inventory.setPrefTileHeight(TILE_SIZE);
        inventory.setAlignment(Pos.TOP_LEFT);
        borderPane.setBottom(inventory);

        VBox infoView = createInfoView();
        borderPane.setRight(infoView);

        scene = new Scene(borderPane, 1100, 575);
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);


        primaryStage.setScene(scene);
        primaryStage.show();

        updateCamera();

        Timeline timeline = new Timeline(new KeyFrame(HERO_MOVE_ANIMATION_DURATION, e -> moveHero()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private VBox createInfoView() {
        VBox infoBox = new VBox();
        infoBox.setAlignment(Pos.TOP_RIGHT);
        infoBox.setSpacing(10);

        enemyCountLabel = new Label();
        updateEnemyCountView(currentEnemyCount);

        infoBox.getChildren().add(enemyCountLabel);

        Button saveAndExit = new Button("Save & Exit");
        saveAndExit.setOnAction(event -> handleSaveAndExit());
        infoBox.getChildren().add(saveAndExit);

        return infoBox;
    }

    private void handleSaveAndExit() {
        session.saveGame().saveCurrentGame();
        controller.getGameWorldService().stop();
        GameContext.resetInstance();
        StartView startView = new StartView(gameApplication);
        try {
            startView.start(primaryStage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateHeroHealthView() {
        if (heroView != null) {
            heroView.updateHealthBar();
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.isControlDown()) {
            controller.heroAttacks();
            logger.info("Control key pressed");
        }

        if (event.getCode() == KeyCode.RIGHT) {
            right = true;
        }
        if (event.getCode() == KeyCode.LEFT) {
            left = true;
        }
        if (event.getCode() == KeyCode.DOWN) {
            down = true;
        }
        if (event.getCode() == KeyCode.UP) {
            up = true;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            up = false;
        }
        if (event.getCode() == KeyCode.DOWN) {
            down = false;
        }
        if (event.getCode() == KeyCode.LEFT) {
            left = false;
        }
        if (event.getCode() == KeyCode.RIGHT) {
            right = false;
        }
    }

    private void moveHero() {
        if (right) {
            controller.moveHeroToRight();
        }
        if (left) {
            controller.moveHeroToLeft();
        }
        if (down) {
            controller.moveHeroDown();
        }
        if (up) {
            controller.moveHeroUp();
        }
    }

    public void handleHeroMoved(IHero hero, int oldX, int oldY) {
        heroAnimator.animateMovement(charactersGrid, heroView, hero.getX(), hero.getY());

        updateHeroHealthView();

        updateCamera();

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

                String imagePath = getImagePathForTile(tile.getType());

                Image tileImage = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));

                final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);
                tileView.setImagePath(imagePath);
                tilesView.put(new Point(tile.getX(), tile.getY()),tileView);

                tilesGrid.add(tileView.getDisplayNode(), x, y);
            }
        }
    }

    private String getImagePathForTile(int tileType) {
        String imagePath = "";

        if (tileType == TileType.WOOD.getType()) {
            imagePath = "/tiles/floor/wood.png";
        }
        else if (tileType == TileType.STONE.getType()) {
            imagePath = "/tiles/floor/stone.png";
        }
        else if (tileType == TileType.WALL.getType()) {
            imagePath = "/tiles/obstacles/wall.png";
        }
        else if (tileType == TileType.GRASS.getType()) {
            imagePath = "/tiles/floor/grass.png";
        }
        else if (tileType == TileType.FLOOR1.getType()) {
            imagePath = "/tiles/floor/floor1.png";
        }
        else if (tileType == TileType.FLOOR2.getType()) {
            imagePath = "/tiles/floor/floor2.png";
        }
        else if (tileType == TileType.PATH.getType()) {
            imagePath = "/tiles/floor/path.png";
        }
        else if (tileType == TileType.GRASS_WITH_STONES.getType()) {
            imagePath = "/tiles/floor/grass_with_stones.png";
        }
        else if (tileType == TileType.DESTRUCTIBLE_WALL.getType()) {
            imagePath = "/tiles/obstacles/destroyable_wall1.png";
        }
        else if (tileType == TileType.EMPTY.getType()) {
            imagePath = "/tiles/floor/empty.png";
        }
        else if (tileType == TileType.EXIT.getType()) {
            imagePath = "/tiles/special/exit.png";
        }
        else if (tileType == TileType.PORTAL.getType()) {
            imagePath = "/tiles/special/portal.png";
        }
        else if (tileType == TileType.WALL_HIDING_PORTAL.getType()) {
            imagePath = "/tiles/obstacles/destroyable_wall1.png";
        }
        else if (tileType == TileType.WALL_HIDING_EXIT.getType()) {
            imagePath = "/tiles/obstacles/wall.png";
        }
        else {
            throw new IllegalArgumentException("Unknown tile type: " + tileType);
        }
        return imagePath;
    }

    public void updateItems(final Collection<IItem> items) {
        // TODO: do not delete views, instead update them
        for (Point point : itemViews.keySet()) {
            ItemView itemView = itemViews.get(point);
            itemsGrid.getChildren().remove(itemView.getDisplayNode());
        }

        for (IItem item : items) {

            final Image itemImage = loadItemImage(item);
            final ItemView itemView = new ItemView(item, itemImage, TILE_SIZE);

            Point point = new Point(item.getX(), item.getY());
            itemViews.put(point, itemView);

            itemsGrid.add(itemView.getDisplayNode(), item.getX(), item.getY());
        }

    }

    public void updateEnemies(Collection<IEnemy> enemies) {
        updateEnemyCountView(enemies.size());

        final Set<IEnemy> enemiesToRemove = new HashSet<>(enemyViews.keySet());

        for (IEnemy enemy : enemies) {
            EnemyView enemyView = enemyViews.get(enemy);

            if (enemyView == null) {
                String imagePath = getImagePathForEnemy(enemy.getType());
                final Image enemyImage = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
                enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);
                enemyView.setTilePosition(enemy.getX(), enemy.getY());
                enemyViews.put(enemy, enemyView);
                charactersGrid.add(enemyView.getDisplayNode(), enemy.getX(), enemy.getY());
            }
            else {
                enemiesToRemove.remove(enemy);
                enemyAnimator.animateMovement(charactersGrid, enemyView, enemy.getX(), enemy.getY());
            }
            enemyView.updateHealthBar();
        }

        for (IEnemy removedEnemy : enemiesToRemove) {
            final EnemyView enemyView = enemyViews.remove(removedEnemy);
            if (enemyView != null) {
                enemyAnimator.cancelAnimation(enemyView);
                charactersGrid.getChildren().remove(enemyView.getDisplayNode());
            }
        }
    }

    private String getImagePathForEnemy(final int enemyType) {
        String imagePath;

        if ( enemyType == TileType.ENEMY_LR.getType()) {
            imagePath = "/tiles/enemies/enemy.png";
        }
        else if ( enemyType == TileType.ENEMY_TD.getType()) {
            imagePath = "/tiles/enemies/enemy2.png";
        }
        else if ( enemyType == TileType.ENEMY_2D.getType()) {
            imagePath = "/tiles/enemies/enemy3.png";
        }
        else if ( enemyType == TileType.ENEMY_FH.getType()) {
            imagePath = "/tiles/enemies/enemy4.png";
        }
        else {
            throw new IllegalArgumentException("Unknown enemy type: " +  enemyType);
        }
        return imagePath;
    }


    private String getImagePathForProjectile(int projectileType) {
        if (projectileType == TileType.PROJECTILE_FIRE.getType()) {
            return "/tiles/items/weapons/bomb.png";
        }

        throw new IllegalArgumentException("Unknown projectile type: " + projectileType);
    }


    public void updateEnemy(IEnemy enemy, int damage) {
        EnemyView enemyView = enemyViews.get(enemy);

        if ((enemyView == null)) {
            throw new IllegalArgumentException("No enemy rectangle found for enemy at position: " + enemy.getX() + ", " + enemy.getY());
        }

        enemyView.updateHealthBar();

        if (enemy.getHealth() <= 0) {
            enemyAnimator.cancelAnimation(enemyView);
            boolean deleted = charactersGrid.getChildren().remove(enemyView.getDisplayNode());

            if (!deleted) {
                throw new IllegalArgumentException("Enemy rectangle not found in grid");
            }

            enemyViews.remove(enemy);
            updateEnemyCountView(controller.getEnemies().size());
        }
    }

    public void handleAllEnemiesDefeated() {
    }

    private void updateEnemyCountView(int enemyCount) {
        currentEnemyCount = enemyCount;

        if (enemyCountLabel == null) {
            return;
        }

        enemyCountLabel.setText("Enemies: " + enemyCount);
    }

    public void handleItemPickedUp(ICharacter element, IItem item) {
        ItemView itemView = removeItem(item);

        if (element instanceof IHero h) {
            if (h.getInventory().containsItem(item)) {
                final ImageView itemImageView = itemView.getImageView();
                itemImageView.setOnMouseClicked(event -> {
                    logger.info("Item in inventory clicked: " + item);
                    controller.onInventarItemClicked(item);
                });

                inventory.getChildren().add(itemImageView);
            }
        }

    }

    private void refreshInventory() {
        inventory.getChildren().clear();
        if (hero == null) {
            return;
        }
        hero.getInventory().getItems().forEach(item -> {
            Image itemImage = loadItemImage(item);
            ItemView itemView = new ItemView(item, itemImage, TILE_SIZE);
            ImageView imageView = itemView.getImageView();
            imageView.setOnMouseClicked(event -> controller.onInventarItemClicked(item));
            inventory.getChildren().add(imageView);
        });
    }

    private Image loadItemImage(IItem item) {
        String imagePath = switch (item.getType()) {
            case 100 -> "/tiles/items/yellow_key.png";
            case 101 -> "/tiles/items/yellow_key_stone.png";
            case 200 -> "/tiles/items/weapons/sword.png";
            case 201 -> "/tiles/items/weapons/sword2.png";
            case 220 -> "/tiles/items/weapons/lance.png";
            case 221 -> "/tiles/items/weapons/double_ended_lance.png";
            case 222 -> "/tiles/items/weapons/multi_spike_lance.png";
            case 230 -> "/tiles/items/weapons/bomb_placer.png";
            case 240 -> "/tiles/items/weapons/sword2.png";
            case 300 -> "/tiles/items/rings/magic_ring1.png";
            case 160 -> "/tiles/items/consumable/health_potion.png";
            case 170 -> "/tiles/items/consumable/poison_potion.png";
            default -> throw new IllegalArgumentException("Unknown item type: " + item.getType());
        };

        return new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    private ItemView removeItem(IItem item) {
        Point point = new Point(item.getX(), item.getY());
        ItemView itemView = itemViews.remove(point);

        if (itemView == null) {
            throw new IllegalArgumentException("No item rectangle found for point: " + point);
        }

        itemsGrid.getChildren().remove(itemView.getDisplayNode());
        return itemView;
    }

    public static void main(String[] args) {
        StartView.main(args);
    }

    public void handleHeroDefeated() {
        logger.log(Level.INFO, "Hero defeated! -> Stop Game");
        scene.setOnKeyPressed(null);
    }

    public void handleHeroHit() {
        updateHeroHealthView();
    }

    private void updateCamera() {
        if (stackPane == null || hero == null) {
            return;
        }

        double heroPixelX = hero.getX() * TILE_SIZE + TILE_SIZE / 2.0;
        double heroPixelY = hero.getY() * TILE_SIZE + TILE_SIZE / 2.0;

        double targetTranslateX = viewportWidth / 2.0 - heroPixelX;
        double targetTranslateY = viewportHeight / 2.0 - heroPixelY;

        double minTranslateX = Math.min(0, viewportWidth - mapPixelWidth);
        double maxTranslateX = Math.max(0, viewportWidth - mapPixelWidth);
        double minTranslateY = Math.min(0, viewportHeight - mapPixelHeight);
        double maxTranslateY = Math.max(0, viewportHeight - mapPixelHeight);

        stackPane.setTranslateX(clamp(targetTranslateX, minTranslateX, maxTranslateX));
        stackPane.setTranslateY(clamp(targetTranslateY, minTranslateY, maxTranslateY));
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private void removeBomb(IBomb bomb) {
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
        bombsGrid.getChildren().remove(bombView.getDisplayNode());
    }

    public void bombExploded(IBomb bomb, List<Point> explosionPoints) {
        logger.info("Bomb exploded: " + bomb);
        removeBomb(bomb);

        addExplosionForBomb(bomb, explosionPoints);
    }

    private void addExplosionForBomb(IBomb bomb, List<Point> explosionPoints) {
        final Image attackImage = new Image(requireNonNull(getClass().getResourceAsStream("/tiles/items/weapons/bomb_explosion.png")));
        for (Point explosionPoint : explosionPoints) {
            final BombView explosionView = new BombView(bomb, attackImage, true, TILE_SIZE);
            bombsViews.add(explosionView);
            bombsGrid.add(explosionView.getDisplayNode(), explosionPoint.getX(), explosionPoint.getY());
        }
    }

    public void bombExplosionFinished(IBomb bomb) {
        logger.info("Bomb explosion finished: " + bomb);

        // Remove all explosions that belong to the bomb
        final List<BombView> explosionsToRemove = new ArrayList<>();

        for (BombView bombView : bombsViews) {
            if (bombView.getTileMapElement().equals(bomb)) {
                if (!bombView.isExplosion()) {
                    throw new IllegalArgumentException("Bomb rectangle is not an explosion: " + bombView);
                }
                bombsGrid.getChildren().remove(bombView.getDisplayNode());
                explosionsToRemove.add(bombView);
            }
        }

        bombsViews.removeAll(explosionsToRemove);
    }

    public synchronized void updateBombs(Collection<IBomb> bombs) {
        logger.info("Update bombs");

        // TODO: do not delete views, instead update them
        final Collection<BombView> bombViewsToRemove = new ArrayList<>();

        for (BombView bombView : bombsViews) {
            if (!bombView.isExplosion()) {
                bombsGrid.getChildren().remove(bombView.getDisplayNode());
                bombViewsToRemove.add(bombView);
            }
        }

        bombsViews.removeAll(bombViewsToRemove);

        for (IBomb bomb : bombs) {
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

            final Image bombImage = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
            final BombView bombView = new BombView(bomb, bombImage, false, TILE_SIZE);

            bombsViews.add(bombView);
            bombsGrid.add(bombView.getDisplayNode(), bomb.getX(), bomb.getY());
        }

    }

    public void addProjectile(IProjectile projectile) {
        String imagePath = getImagePathForProjectile(projectile.getType());
        final Image projectileImage = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
        final ProjectileView projectileView = new ProjectileView(projectile, projectileImage, TILE_SIZE);
        projectileView.setTilePosition(projectile.getX(), projectile.getY());
        projectileViews.put(projectile, projectileView);
        projectilesGrid.add(projectileView.getDisplayNode(), projectile.getX(), projectile.getY());
    }

    public void updateProjectile(IProjectile projectile) {
        final ProjectileView projectileView = projectileViews.get(projectile);
        if (projectileView == null) {
            addProjectile(projectile);
            return;
        }

        projectileAnimator.animateMovement(projectilesGrid, projectileView, projectile.getX(), projectile.getY());
    }

    public void removeProjectile(IProjectile projectile) {
        final ProjectileView projectileView = projectileViews.remove(projectile);
        if (projectileView != null) {
            projectileAnimator.cancelAnimation(projectileView);
            projectilesGrid.getChildren().remove(projectileView.getDisplayNode());
        }
    }

    public void handleTileHit(IWeapon weapon, Tile tile) {
        if (tile.isDestroyed()) {
            logger.info("Tile already destroyed: " + tile);
            Point point = new Point(tile.getX(), tile.getY());
            final TileView oldTileView = tilesView.get(point);

            if (oldTileView == null) {
                throw new IllegalArgumentException("No tile rectangle found for point: " + point);
            }

            String destroyedImagePath = "";

            if (tile.canTransformToNewTileType()) {
                // TODO: update of the view should be done, when the tiles are exchanged in the tilemap.
                //       Here the view "guesses" that it has to be exchanged.
                destroyedImagePath = getImagePathForTile(tile.getTransformToNewTileType());
            }
            else {
                String[] split = oldTileView.getImagePath().split("\\.");
                destroyedImagePath = split[0] + "_destroyed." + split[1];
            }


            final Image tileImage = new Image(requireNonNull(getClass().getResourceAsStream(destroyedImagePath)));
            final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);


            tilesGrid.getChildren().remove(oldTileView.getDisplayNode());
            tilesView.remove(point);

            tilesGrid.add(tileView.getDisplayNode(), tile.getX(), tile.getY());
            tilesView.put(point, tileView);
        }
    }


}