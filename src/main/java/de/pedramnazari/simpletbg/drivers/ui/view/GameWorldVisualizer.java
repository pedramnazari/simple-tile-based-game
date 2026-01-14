package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.drivers.GameInitializer;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.drivers.ui.controller.InputState;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
    private static final Duration RUSH_CREATURE_MOVE_ANIMATION_DURATION = Duration.millis(80);
    private static final Duration PROJECTILE_MOVE_ANIMATION_DURATION = Duration.millis(90);
    private static final int VISIBLE_COLUMNS = 15;
    private static final int VISIBLE_ROWS = 11;

    private final Map<Point, ItemView> itemViews = new HashMap<>();
    private final Map<IEnemy, EnemyView> enemyViews = new HashMap<>();
    private final Map<ICompanion, CompanionView> companionViews = new HashMap<>();
    private final Collection<BombView> bombsViews = new ArrayList<>();
    private final Map<IProjectile, ProjectileView> projectileViews = new HashMap<>();
    private GridPane tilesGrid;
    private GridPane itemsGrid;
    private GridPane bombsGrid;
    private GridPane projectilesGrid;
    private GridPane charactersGrid;
    private Pane effectsLayer;
    private Scene scene;
    private HeroView heroView;
    private TilePane inventory;
    private GameWorldController controller;
    private IHero hero;
    private StackPane stackPane;
    private Pane cameraViewport;
    private double viewportWidth;
    private double viewportHeight;
    private double mapPixelWidth;
    private double mapPixelHeight;

    private final InputState inputState = new InputState();
    private GameMapDefinition mapDefinition;
    private Map<Point, TileView> tilesView = new HashMap<>();
    private Label enemyCountLabel;
    private int currentEnemyCount;
    private final TileMapElementAnimator heroAnimator = new TileMapElementAnimator(HERO_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private final TileMapElementAnimator enemyAnimator = new TileMapElementAnimator(ENEMY_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private final TileMapElementAnimator rushCreatureAnimator = new TileMapElementAnimator(RUSH_CREATURE_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private final TileMapElementAnimator projectileAnimator = new TileMapElementAnimator(PROJECTILE_MOVE_ANIMATION_DURATION, TILE_SIZE);
    private CameraController cameraController;
    
    // Image cache to avoid reloading resources
    private final Map<String, Image> imageCache = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        GameMapDefinition selectedMap = mapDefinition != null ? mapDefinition : GameMaps.defaultMap();
        this.mapDefinition = selectedMap;

        controller = GameInitializer.initAndStartGame(selectedMap);
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
        
        // Create effects layer for visual effects like lightning bolts
        effectsLayer = new Pane();
        effectsLayer.setPrefSize(mapPixelWidth, mapPixelHeight);
        effectsLayer.setMouseTransparent(true); // Don't intercept mouse events

        initFloorAndObstacleTiles(controller.getTileMap());

        updateItems(controller.getItems());
        Collection<IEnemy> enemies = controller.getEnemies();
        logger.log(Level.INFO, "Enemies: {0}", enemies.size());
        currentEnemyCount = enemies.size();
        updateEnemies(enemies);
        
        // Initialize companions
        Collection<ICompanion> companions = controller.getCompanions();
        logger.log(Level.INFO, "Companions: {0}", companions.size());
        updateCompanions(companions);

        // add hero to grid
        final Image heroImage = getCachedImage("/tiles/hero/hero.png");
        heroView = new HeroView(hero, heroImage, TILE_SIZE);

        charactersGrid.add(heroView.getDisplayNode(), hero.getX(), hero.getY());

        updateHeroHealthView();

        stackPane.getChildren().addAll(tilesGrid, itemsGrid, bombsGrid, projectilesGrid, charactersGrid, effectsLayer);

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

        // Initialize camera controller for smooth tracking
        cameraController = new CameraController(stackPane, viewportWidth, viewportHeight, mapPixelWidth, mapPixelHeight);
        cameraController.initializeCameraPosition(heroView.getDisplayNode(), hero.getX(), hero.getY(), TILE_SIZE);

        // Setup continuous camera update for smooth scrolling during animations
        Timeline cameraTimeline = new Timeline(new KeyFrame(Duration.millis(16), e -> updateCamera())); // ~60fps
        cameraTimeline.setCycleCount(Timeline.INDEFINITE);
        cameraTimeline.play();

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

        return infoBox;
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
            inputState.setRight(true);
        }
        if (event.getCode() == KeyCode.LEFT) {
            inputState.setLeft(true);
        }
        if (event.getCode() == KeyCode.DOWN) {
            inputState.setDown(true);
        }
        if (event.getCode() == KeyCode.UP) {
            inputState.setUp(true);
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            inputState.setUp(false);
        }
        if (event.getCode() == KeyCode.DOWN) {
            inputState.setDown(false);
        }
        if (event.getCode() == KeyCode.LEFT) {
            inputState.setLeft(false);
        }
        if (event.getCode() == KeyCode.RIGHT) {
            inputState.setRight(false);
        }
    }

    // Execute at most one movement command per tick
    // Priority: vertical over horizontal (for consistent diagonal feel)
    private void moveHero() {
        if (!inputState.hasMovementInput()) {
            return;
        }

        int[] movement = inputState.getDiscreteMovement();
        int dx = movement[0];
        int dy = movement[1];

        // Execute one move per tick - prioritize vertical for consistent diagonal
        if (dy != 0) {
            if (dy > 0) {
                controller.moveHeroDown();
            } else {
                controller.moveHeroUp();
            }
        } else if (dx != 0) {
            if (dx > 0) {
                controller.moveHeroToRight();
            } else {
                controller.moveHeroToLeft();
            }
        }
    }

    public void handleHeroMoved(IHero hero, int oldX, int oldY) {
        heroAnimator.animateMovement(charactersGrid, heroView, hero.getX(), hero.getY());
        updateHeroHealthView();
        // Camera updates continuously in its own timeline
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

                Image tileImage = getCachedImage(imagePath);

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

    // Helper to load and cache images
    private Image getCachedImage(String imagePath) {
        return imageCache.computeIfAbsent(imagePath, path -> 
            new Image(requireNonNull(getClass().getResourceAsStream(path)))
        );
    }

    public void updateItems(final Collection<IItem> items) {
        // Incremental update: reuse existing views, only create new ones as needed
        Set<Point> currentPositions = new HashSet<>();
        int reusedCount = 0;
        int createdCount = 0;
        
        for (IItem item : items) {
            Point point = new Point(item.getX(), item.getY());
            currentPositions.add(point);
            
            ItemView existingView = itemViews.get(point);
            
            // Only create new view if one doesn't exist at this position
            if (existingView == null) {
                String imagePath = switch (item.getType()) {
                    case 100 -> "/tiles/items/yellow_key.png";
                    case 101 -> "/tiles/items/yellow_key_stone.png";
                    case 200 -> "/tiles/items/weapons/sword.png";
                    case 201 -> "/tiles/items/weapons/sword2.png";
                    case 220 -> "/tiles/items/weapons/lance.png";
                    case 221 -> "/tiles/items/weapons/double_ended_lance.png";
                    case 222 -> "/tiles/items/weapons/multi_spike_lance.png";
                    case 230 -> "/tiles/items/weapons/bomb_placer.png";
                    case 240 -> "/tiles/items/weapons/fire_staff.png";
                    case 241 -> "/tiles/items/weapons/ice_wand.png";
                    case 242 -> "/tiles/items/weapons/lightning_rod.png";
                    case 300 -> "/tiles/items/rings/magic_ring1.png";
                    case 350 -> "/tiles/items/armor/auto_attack_armor.png";
                    case 160 -> "/tiles/items/consumable/health_potion.png";
                    case 170 -> "/tiles/items/consumable/poison_potion.png";
                    default -> throw new IllegalArgumentException("Unknown item type: " + item.getType());
                };

                final Image itemImage = getCachedImage(imagePath);
                final ItemView itemView = new ItemView(item, itemImage, TILE_SIZE);

                itemViews.put(point, itemView);
                itemsGrid.add(itemView.getDisplayNode(), item.getX(), item.getY());
                createdCount++;
            } else {
                reusedCount++;
            }
        }
        
        // Remove views for items that no longer exist
        Set<Point> toRemove = new HashSet<>();
        for (Point point : itemViews.keySet()) {
            if (!currentPositions.contains(point)) {
                ItemView itemView = itemViews.get(point);
                itemsGrid.getChildren().remove(itemView.getDisplayNode());
                toRemove.add(point);
            }
        }
        int removedCount = toRemove.size();
        toRemove.forEach(itemViews::remove);
        
        // Log stats for verification (only when changes occur)
        if (createdCount > 0 || removedCount > 0) {
            logger.log(Level.FINE, "Item update: reused={0}, created={1}, removed={2}", 
                new Object[]{reusedCount, createdCount, removedCount});
        }
    }

    public void updateEnemies(Collection<IEnemy> enemies) {
        updateEnemyCountView(enemies.size());

        final Set<IEnemy> enemiesToRemove = new HashSet<>(enemyViews.keySet());

        // Use a counter to stagger animations and avoid synchronous movement
        int animationDelayCounter = 0;
        
        for (IEnemy enemy : enemies) {
            EnemyView enemyView = enemyViews.get(enemy);

            if (enemyView == null) {
                String imagePath = getImagePathForEnemy(enemy.getType());
                final Image enemyImage = getCachedImage(imagePath);
                enemyView = new EnemyView(enemy, enemyImage, TILE_SIZE);
                enemyView.setTilePosition(enemy.getX(), enemy.getY());
                enemyViews.put(enemy, enemyView);
                charactersGrid.add(enemyView.getDisplayNode(), enemy.getX(), enemy.getY());
                
                // Add spawn effect for rush creatures
                if (enemy.getType() == TileType.ENEMY_RUSH_CREATURE.getType()) {
                    addSpawnEffect(enemy.getX(), enemy.getY());
                }
            }
            else {
                enemiesToRemove.remove(enemy);
                
                // Add staggered delay to break synchronization
                // Rush creatures get minimal delay for fluent movement
                final int delayMs;
                if (enemy.getType() == TileType.ENEMY_RUSH_CREATURE.getType()) {
                    // Rush creatures: very small stagger (0-40ms) for fluent, unsynchronized movement
                    delayMs = (animationDelayCounter * 10) % 40;
                } else {
                    // Regular enemies: longer stagger (0-120ms)
                    delayMs = (animationDelayCounter * 25) % 120;
                }
                
                final EnemyView finalEnemyView = enemyView;
                final int finalX = enemy.getX();
                final int finalY = enemy.getY();
                final boolean isRushCreature = enemy.getType() == TileType.ENEMY_RUSH_CREATURE.getType();
                
                // Delay the animation to stagger enemy movements
                if (delayMs > 0) {
                    javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(delayMs));
                    pause.setOnFinished(e -> {
                        if (isRushCreature) {
                            rushCreatureAnimator.animateMovement(charactersGrid, finalEnemyView, finalX, finalY);
                        } else {
                            enemyAnimator.animateMovement(charactersGrid, finalEnemyView, finalX, finalY);
                        }
                    });
                    pause.play();
                } else {
                    // No delay for first enemy
                    if (isRushCreature) {
                        rushCreatureAnimator.animateMovement(charactersGrid, finalEnemyView, finalX, finalY);
                    } else {
                        enemyAnimator.animateMovement(charactersGrid, finalEnemyView, finalX, finalY);
                    }
                }
                
                animationDelayCounter++;
            }
            enemyView.updateHealthBar();
        }

        for (IEnemy removedEnemy : enemiesToRemove) {
            final EnemyView enemyView = enemyViews.remove(removedEnemy);
            if (enemyView != null) {
                // Cancel animation with appropriate animator
                if (removedEnemy.getType() == TileType.ENEMY_RUSH_CREATURE.getType()) {
                    rushCreatureAnimator.cancelAnimation(enemyView);
                } else {
                    enemyAnimator.cancelAnimation(enemyView);
                }
                charactersGrid.getChildren().remove(enemyView.getDisplayNode());
            }
        }
    }

    public void updateCompanions(Collection<ICompanion> companions) {
        final Set<ICompanion> companionsToRemove = new HashSet<>(companionViews.keySet());
        
        for (ICompanion companion : companions) {
            CompanionView companionView = companionViews.get(companion);
            
            if (companionView == null) {
                String imagePath = getImagePathForCompanion(companion.getType());
                final Image companionImage = getCachedImage(imagePath);
                companionView = new CompanionView(companion, companionImage, TILE_SIZE);
                companionView.setTilePosition(companion.getX(), companion.getY());
                companionViews.put(companion, companionView);
                charactersGrid.add(companionView.getDisplayNode(), companion.getX(), companion.getY());
            } else {
                companionsToRemove.remove(companion);
                
                final CompanionView finalCompanionView = companionView;
                final int finalX = companion.getX();
                final int finalY = companion.getY();
                
                // Animate companion movement with rush creature animator for fast, fluid movement
                rushCreatureAnimator.animateMovement(charactersGrid, finalCompanionView, finalX, finalY);
            }
            companionView.updateHealthBar();
        }
        
        for (ICompanion removedCompanion : companionsToRemove) {
            final CompanionView companionView = companionViews.remove(removedCompanion);
            if (companionView != null) {
                rushCreatureAnimator.cancelAnimation(companionView);
                charactersGrid.getChildren().remove(companionView.getDisplayNode());
            }
        }
    }
    
    private String getImagePathForCompanion(final int companionType) {
        if (companionType == TileType.COMPANION_HUSKY.getType()) {
            return "/tiles/companions/husky.png";
        }
        throw new IllegalArgumentException("Unknown companion type: " + companionType);
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
        else if ( enemyType == TileType.ENEMY_SUMMONER.getType()) {
            imagePath = "/tiles/enemies/enemy_summoner.png";
        }
        else if ( enemyType == TileType.ENEMY_RUSH_CREATURE.getType()) {
            imagePath = "/tiles/enemies/enemy_rush.png";
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
        else if (projectileType == TileType.PROJECTILE_ICE.getType()) {
            return "/tiles/items/weapons/bomb.png"; // Using same image for now
        }
        else if (projectileType == TileType.PROJECTILE_LIGHTNING.getType()) {
            return "/tiles/items/weapons/bomb.png"; // Using same image for now
        }
        else if (projectileType == TileType.PROJECTILE_WIND.getType()) {
            return "/tiles/projectiles/wind_projectile.png";
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

    public void setMapDefinition(GameMapDefinition mapDefinition) {
        this.mapDefinition = mapDefinition;
    }

    public void handleHeroDefeated() {
        logger.log(Level.INFO, "Hero defeated! -> Stop Game");
        scene.setOnKeyPressed(null);
    }

    public void handleHeroHit() {
        updateHeroHealthView();
    }

    private void updateCamera() {
        if (cameraController == null || hero == null || heroView == null) {
            return;
        }
        // Track hero's actual rendered position including animation translate
        cameraController.updateCameraToFollowNode(heroView.getDisplayNode(), hero.getX(), hero.getY(), TILE_SIZE);
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
        final Image attackImage = getCachedImage("/tiles/items/weapons/bomb_explosion.png");
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
        // Incremental update: only remove non-explosion bomb views that aren't in the current bomb list
        Set<IBomb> currentBombs = new HashSet<>(bombs);
        Collection<BombView> toRemove = new ArrayList<>();
        int reusedCount = 0;
        int createdCount = 0;
        
        for (BombView bombView : bombsViews) {
            if (!bombView.isExplosion() && !currentBombs.contains(bombView.getTileMapElement())) {
                bombsGrid.getChildren().remove(bombView.getDisplayNode());
                toRemove.add(bombView);
            } else if (!bombView.isExplosion()) {
                reusedCount++;
            }
        }
        int removedCount = toRemove.size();
        bombsViews.removeAll(toRemove);

        // Add new bombs (skip those that already have views or are exploding)
        for (IBomb bomb : bombs) {
            if (bomb.isExplosionOngoing()) {
                continue; // Skip bombs that are currently exploding
            }
            
            // Check if view already exists
            boolean viewExists = false;
            for (BombView bombView : bombsViews) {
                if (bombView.getTileMapElement().equals(bomb)) {
                    viewExists = true;
                    break;
                }
            }
            
            if (!viewExists) {
                String imagePath = switch (bomb.getType()) {
                    case 231 -> "/tiles/items/weapons/bomb.png";
                    default -> throw new IllegalArgumentException("Unknown bomb type: " + bomb.getType());
                };

                final Image bombImage = getCachedImage(imagePath);
                final BombView bombView = new BombView(bomb, bombImage, false, TILE_SIZE);

                bombsViews.add(bombView);
                bombsGrid.add(bombView.getDisplayNode(), bomb.getX(), bomb.getY());
                createdCount++;
            }
        }
        
        // Log stats for verification (only when changes occur)
        if (createdCount > 0 || removedCount > 0) {
            logger.log(Level.INFO, "Bomb update: reused={0}, created={1}, removed={2}", 
                new Object[]{reusedCount, createdCount, removedCount});
        }
    }

    public void addProjectile(IProjectile projectile) {
        String imagePath = getImagePathForProjectile(projectile.getType());
        final Image projectileImage = getCachedImage(imagePath);
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
            
            // Show impact effect before removing
            showProjectileImpactEffect(projectile, projectileView);
            
            projectilesGrid.getChildren().remove(projectileView.getDisplayNode());
        }
    }

    /**
     * Show impact visual effect when projectile hits or expires
     */
    private void showProjectileImpactEffect(IProjectile projectile, ProjectileView projectileView) {
        int projectileType = projectile.getType();
        int x = projectile.getX();
        int y = projectile.getY();
        
        if (projectileType == TileType.PROJECTILE_ICE.getType()) {
            showIceImpactEffect(x, y);
        } else if (projectileType == TileType.PROJECTILE_LIGHTNING.getType()) {
            showLightningImpactEffect(x, y);
        }
    }

    /**
     * Show ice impact: frost burst effect
     */
    private void showIceImpactEffect(int tileX, int tileY) {
        javafx.scene.shape.Circle frostBurst = new javafx.scene.shape.Circle(TILE_SIZE / 2, javafx.scene.paint.Color.CYAN);
        frostBurst.setOpacity(0.6);
        projectilesGrid.add(frostBurst, tileX, tileY);
        
        // Animate the burst - expand and fade
        javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(300), frostBurst);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(2.0);
        scale.setToY(2.0);
        
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), frostBurst);
        fade.setFromValue(0.6);
        fade.setToValue(0.0);
        
        // Clean up after animation
        fade.setOnFinished(event -> projectilesGrid.getChildren().remove(frostBurst));
        
        scale.play();
        fade.play();
    }

    /**
     * Show lightning impact: spark burst effect
     */
    private void showLightningImpactEffect(int tileX, int tileY) {
        javafx.scene.shape.Circle sparkBurst = new javafx.scene.shape.Circle(TILE_SIZE / 3, javafx.scene.paint.Color.YELLOW);
        sparkBurst.setOpacity(0.9);
        
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(1.0);
        sparkBurst.setEffect(glow);
        
        projectilesGrid.add(sparkBurst, tileX, tileY);
        
        // Animate the burst - quick flash
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), sparkBurst);
        fade.setFromValue(0.9);
        fade.setToValue(0.0);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        
        // Clean up after animation
        fade.setOnFinished(event -> projectilesGrid.getChildren().remove(sparkBurst));
        
        fade.play();
    }

    /**
     * Show chain lightning arc from one enemy to another with animated traveling bolt
     */
    public void showChainLightningArc(int fromX, int fromY, int toX, int toY) {
        // Calculate pixel coordinates
        double startPixelX = fromX * TILE_SIZE + TILE_SIZE / 2.0;
        double startPixelY = fromY * TILE_SIZE + TILE_SIZE / 2.0;
        double endPixelX = toX * TILE_SIZE + TILE_SIZE / 2.0;
        double endPixelY = toY * TILE_SIZE + TILE_SIZE / 2.0;
        
        // Create the main lightning bolt path with jagged segments for realistic look
        javafx.scene.shape.Path lightningPath = createJaggedLightningPath(startPixelX, startPixelY, endPixelX, endPixelY);
        lightningPath.setStroke(javafx.scene.paint.Color.rgb(255, 255, 150)); // Bright yellow-white
        lightningPath.setStrokeWidth(3);
        lightningPath.setOpacity(0.0); // Start invisible
        lightningPath.setFill(null);
        
        // Add intense glow effect
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(1.0);
        javafx.scene.effect.DropShadow dropShadow = new javafx.scene.effect.DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.YELLOW);
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.7);
        glow.setInput(dropShadow);
        lightningPath.setEffect(glow);
        
        // Add to the effects layer (not stackPane directly)
        effectsLayer.getChildren().add(lightningPath);
        
        // Create a traveling spark effect
        javafx.scene.shape.Circle travelingSpark = new javafx.scene.shape.Circle(6, javafx.scene.paint.Color.WHITE);
        travelingSpark.setOpacity(0.0);
        javafx.scene.effect.Glow sparkGlow = new javafx.scene.effect.Glow(1.0);
        travelingSpark.setEffect(sparkGlow);
        effectsLayer.getChildren().add(travelingSpark);
        
        // Animate: First make the bolt appear quickly
        javafx.animation.FadeTransition boltAppear = new javafx.animation.FadeTransition(javafx.util.Duration.millis(50), lightningPath);
        boltAppear.setFromValue(0.0);
        boltAppear.setToValue(0.95);
        
        // Animate the spark traveling along the bolt
        javafx.animation.Timeline sparkTravel = new javafx.animation.Timeline();
        int travelSteps = 15;
        for (int i = 0; i <= travelSteps; i++) {
            final double progress = (double) i / travelSteps;
            final double sparkX = startPixelX + (endPixelX - startPixelX) * progress;
            final double sparkY = startPixelY + (endPixelY - startPixelY) * progress;
            final double opacity = i == 0 ? 0.0 : (i == travelSteps ? 0.0 : 0.9);
            
            javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(i * 25),
                e -> {
                    travelingSpark.setCenterX(sparkX);
                    travelingSpark.setCenterY(sparkY);
                    travelingSpark.setOpacity(opacity);
                }
            );
            sparkTravel.getKeyFrames().add(keyFrame);
        }
        
        // Bolt flickers while spark travels
        javafx.animation.FadeTransition boltFlicker = new javafx.animation.FadeTransition(javafx.util.Duration.millis(80), lightningPath);
        boltFlicker.setFromValue(0.95);
        boltFlicker.setToValue(0.6);
        boltFlicker.setCycleCount(5);
        boltFlicker.setAutoReverse(true);
        
        // Final fade out
        javafx.animation.FadeTransition boltFadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(150), lightningPath);
        boltFadeOut.setFromValue(0.6);
        boltFadeOut.setToValue(0.0);
        
        // Clean up after animation
        boltFadeOut.setOnFinished(event -> {
            effectsLayer.getChildren().remove(lightningPath);
            effectsLayer.getChildren().remove(travelingSpark);
        });
        
        // Play animations in sequence
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(
            boltAppear,
            new javafx.animation.ParallelTransition(sparkTravel, boltFlicker),
            boltFadeOut
        );
        sequence.play();
    }
    
    /**
     * Create a jagged lightning path between two points for realistic lightning appearance.
     * Note: Uses Math.random() for visual variety. The randomness is intentional - 
     * each lightning bolt should look unique. This doesn't affect gameplay logic
     * and is purely cosmetic.
     */
    private javafx.scene.shape.Path createJaggedLightningPath(double startX, double startY, double endX, double endY) {
        javafx.scene.shape.Path path = new javafx.scene.shape.Path();
        
        // Calculate direction and distance
        double dx = endX - startX;
        double dy = endY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // If distance is very short, just draw a straight line
        if (distance < TILE_SIZE) {
            path.getElements().add(new javafx.scene.shape.MoveTo(startX, startY));
            path.getElements().add(new javafx.scene.shape.LineTo(endX, endY));
            return path;
        }
        
        // Create jagged segments
        int numSegments = Math.max(3, (int)(distance / (TILE_SIZE * 0.7)));
        path.getElements().add(new javafx.scene.shape.MoveTo(startX, startY));
        
        for (int i = 1; i < numSegments; i++) {
            double progress = (double) i / numSegments;
            double baseX = startX + dx * progress;
            double baseY = startY + dy * progress;
            
            // Add random perpendicular offset for jagged appearance
            double perpX = -dy / distance;
            double perpY = dx / distance;
            double offset = (Math.random() - 0.5) * TILE_SIZE * 0.4;
            
            double jaggedX = baseX + perpX * offset;
            double jaggedY = baseY + perpY * offset;
            
            path.getElements().add(new javafx.scene.shape.LineTo(jaggedX, jaggedY));
        }
        
        path.getElements().add(new javafx.scene.shape.LineTo(endX, endY));
        return path;
    }

    /**
     * Show spawn effect when a summoner creates a rush creature
     */
    private void addSpawnEffect(int tileX, int tileY) {
        // Calculate pixel coordinates
        double centerX = tileX * TILE_SIZE + TILE_SIZE / 2.0;
        double centerY = tileY * TILE_SIZE + TILE_SIZE / 2.0;
        
        // Create a magic circle effect
        javafx.scene.shape.Circle outerCircle = new javafx.scene.shape.Circle(TILE_SIZE / 4, javafx.scene.paint.Color.PURPLE);
        outerCircle.setOpacity(0.0);
        outerCircle.setFill(null);
        outerCircle.setStroke(javafx.scene.paint.Color.PURPLE);
        outerCircle.setStrokeWidth(3);
        outerCircle.setCenterX(centerX);
        outerCircle.setCenterY(centerY);
        
        // Add glow effect
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.8);
        outerCircle.setEffect(glow);
        
        effectsLayer.getChildren().add(outerCircle);
        
        // Animate: expand and fade
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(100), outerCircle);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.8);
        
        javafx.animation.ScaleTransition expand = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), outerCircle);
        expand.setFromX(0.5);
        expand.setFromY(0.5);
        expand.setToX(2.0);
        expand.setToY(2.0);
        
        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(150), outerCircle);
        fadeOut.setFromValue(0.8);
        fadeOut.setToValue(0.0);
        
        // Clean up after animation
        fadeOut.setOnFinished(event -> effectsLayer.getChildren().remove(outerCircle));
        
        // Play animations in sequence
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(
            new javafx.animation.ParallelTransition(fadeIn, expand),
            fadeOut
        );
        sequence.play();
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


            final Image tileImage = getCachedImage(destroyedImagePath);
            final TileView tileView = new TileView(tile, tileImage, TILE_SIZE);


            tilesGrid.getChildren().remove(oldTileView.getDisplayNode());
            tilesView.remove(point);

            tilesGrid.add(tileView.getDisplayNode(), tile.getX(), tile.getY());
            tilesView.put(point, tileView);
        }
    }

    /**
     * Show armor auto-attack visual effects
     */
    public void showArmorAttackEffect(IHero hero, IEnemy target, IArmor armor) {
        int heroX = hero.getX();
        int heroY = hero.getY();
        int targetX = target.getX();
        int targetY = target.getY();
        
        double heroPixelX = heroX * TILE_SIZE + TILE_SIZE / 2.0;
        double heroPixelY = heroY * TILE_SIZE + TILE_SIZE / 2.0;
        double targetPixelX = targetX * TILE_SIZE + TILE_SIZE / 2.0;
        double targetPixelY = targetY * TILE_SIZE + TILE_SIZE / 2.0;
        
        // 1. Brief glow on hero (armor effect)
        showArmorGlow(heroPixelX, heroPixelY);
        
        // 2. Projectile arc from hero to enemy
        showArmorProjectile(heroPixelX, heroPixelY, targetPixelX, targetPixelY);
        
        // 3. Impact flash on enemy (delayed to sync with projectile)
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(200));
        pause.setOnFinished(e -> {
            showImpactFlash(targetPixelX, targetPixelY);
            showParticleBurst(targetPixelX, targetPixelY);
            showScreenShake();
        });
        pause.play();
    }
    
    /**
     * Show a brief glow around the hero (armor glow)
     */
    private void showArmorGlow(double centerX, double centerY) {
        javafx.scene.shape.Circle glowCircle = new javafx.scene.shape.Circle(TILE_SIZE * 0.6, javafx.scene.paint.Color.CYAN);
        glowCircle.setOpacity(0.0);
        glowCircle.setFill(null);
        glowCircle.setStroke(javafx.scene.paint.Color.CYAN);
        glowCircle.setStrokeWidth(3);
        glowCircle.setCenterX(centerX);
        glowCircle.setCenterY(centerY);
        
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(1.0);
        javafx.scene.effect.DropShadow dropShadow = new javafx.scene.effect.DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.CYAN);
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.8);
        glow.setInput(dropShadow);
        glowCircle.setEffect(glow);
        
        effectsLayer.getChildren().add(glowCircle);
        
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(80), glowCircle);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.9);
        
        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(150), glowCircle);
        fadeOut.setFromValue(0.9);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> effectsLayer.getChildren().remove(glowCircle));
        
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(fadeIn, fadeOut);
        sequence.play();
    }
    
    /**
     * Show a projectile arc from hero to target
     */
    private void showArmorProjectile(double startX, double startY, double endX, double endY) {
        javafx.scene.shape.Circle projectile = new javafx.scene.shape.Circle(6, javafx.scene.paint.Color.LIGHTBLUE);
        projectile.setCenterX(startX);
        projectile.setCenterY(startY);
        projectile.setOpacity(0.0);
        
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.9);
        projectile.setEffect(glow);
        
        effectsLayer.getChildren().add(projectile);
        
        // Animate projectile moving to target
        javafx.animation.TranslateTransition move = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(200), projectile);
        move.setFromX(0);
        move.setFromY(0);
        move.setToX(endX - startX);
        move.setToY(endY - startY);
        
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(50), projectile);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(50), projectile);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(javafx.util.Duration.millis(150));
        fadeOut.setOnFinished(event -> effectsLayer.getChildren().remove(projectile));
        
        javafx.animation.ParallelTransition parallel = new javafx.animation.ParallelTransition(move, fadeIn, fadeOut);
        parallel.play();
    }
    
    /**
     * Show an impact flash on the target
     */
    private void showImpactFlash(double centerX, double centerY) {
        javafx.scene.shape.Circle flash = new javafx.scene.shape.Circle(TILE_SIZE * 0.4, javafx.scene.paint.Color.WHITE);
        flash.setCenterX(centerX);
        flash.setCenterY(centerY);
        flash.setOpacity(0.0);
        
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(1.0);
        flash.setEffect(glow);
        
        effectsLayer.getChildren().add(flash);
        
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(50), flash);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(100), flash);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> effectsLayer.getChildren().remove(flash));
        
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(fadeIn, fadeOut);
        sequence.play();
    }
    
    /**
     * Show a particle burst effect
     */
    private void showParticleBurst(double centerX, double centerY) {
        int particleCount = 8;
        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double endX = centerX + Math.cos(angle) * TILE_SIZE * 0.5;
            double endY = centerY + Math.sin(angle) * TILE_SIZE * 0.5;
            
            javafx.scene.shape.Circle particle = new javafx.scene.shape.Circle(3, javafx.scene.paint.Color.ORANGE);
            particle.setCenterX(centerX);
            particle.setCenterY(centerY);
            particle.setOpacity(1.0);
            
            effectsLayer.getChildren().add(particle);
            
            javafx.animation.TranslateTransition move = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(250), particle);
            move.setToX(endX - centerX);
            move.setToY(endY - centerY);
            
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(250), particle);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(event -> effectsLayer.getChildren().remove(particle));
            
            javafx.animation.ParallelTransition parallel = new javafx.animation.ParallelTransition(move, fade);
            parallel.play();
        }
    }
    
    /**
     * Show a subtle screen shake effect
     */
    private void showScreenShake() {
        double shakeAmount = 3.0;
        javafx.animation.Timeline shake = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(0), e -> {
                stackPane.setTranslateX(shakeAmount);
                stackPane.setTranslateY(shakeAmount);
            }),
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(30), e -> {
                stackPane.setTranslateX(-shakeAmount);
                stackPane.setTranslateY(-shakeAmount);
            }),
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(60), e -> {
                stackPane.setTranslateX(shakeAmount * 0.5);
                stackPane.setTranslateY(shakeAmount * 0.5);
            }),
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(90), e -> {
                stackPane.setTranslateX(0);
                stackPane.setTranslateY(0);
            })
        );
        shake.play();
    }


}