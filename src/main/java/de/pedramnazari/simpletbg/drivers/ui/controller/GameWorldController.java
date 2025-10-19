package de.pedramnazari.simpletbg.drivers.ui.controller;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyObserver;
import de.pedramnazari.simpletbg.drivers.ui.view.GameWorldVisualizer;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.bomb.IBombEventListener;
import de.pedramnazari.simpletbg.inventory.service.projectile.IProjectileEventListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.event.*;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IHeroHitListener;
import de.pedramnazari.simpletbg.tilemap.service.IHeroMovedListener;
import de.pedramnazari.simpletbg.tilemap.service.ITileHitListener;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWorldController implements IEnemyObserver, IItemPickUpListener, IEnemyHitListener, IHeroHitListener, IBombEventListener, ITileHitListener, IHeroMovedListener, IItemEventListener, IProjectileEventListener {

    private static final Logger logger = Logger.getLogger(GameWorldController.class.getName());

    private final GameWorldService gameWorldService;
    // TODO: remove this dependency
    private GameWorldVisualizer gameWorldVisualizer;

    public GameWorldController(final GameWorldService gameWorldService) {
        this.gameWorldService = gameWorldService;
    }

    public void setTileMapVisualizer(GameWorldVisualizer gameWorldVisualizer) {
        this.gameWorldVisualizer = gameWorldVisualizer;
    }

    public TileMap startGameUsingMap(Tile[][] tiles, int heroX, int heroY) {
        return gameWorldService.createAndInitMap(tiles, List.of(), List.of(), heroX, heroY);
    }

    public void startGameUsingMap(final Tile[][] tiles, Collection<IItem> items, Collection<IEnemy> enemiesConfig, int heroX, int heroY) {
        gameWorldService.createAndInitMap(tiles, items, enemiesConfig, heroX, heroY);

        GameContext.initialize(gameWorldService.getTileMap(), gameWorldService.getItemService(), gameWorldService.getHeroService(), gameWorldService.getEnemyService(), "0");

        gameWorldService.start();
    }

    public void moveHeroToRight() {
        gameWorldService.moveHeroToRight();
    }

    public void moveHeroToLeft() {
        gameWorldService.moveHeroToLeft();
    }

    public void moveHeroUp() {
        gameWorldService.moveHeroUp();
    }

    public void moveHeroDown() {
        gameWorldService.moveHeroDown();
    }

    public Collection<IItem> getItems() {
        return gameWorldService.getItemService().getItems();
    }


    public TileMap getTileMap() {
        return gameWorldService.getTileMap();
    }

    public IHero getHero() {
        return gameWorldService.getHero();
    }

    public Collection<IEnemy> getEnemies() {
        return gameWorldService.getEnemies();
    }

    @Override
    public void update(Collection<IEnemy> enemies) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateEnemies(enemies));
    }

    @Override
    public void onItemPickedUp(ICharacter character, IItem item) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.handleItemPickedUp(character, item));
    }

    public void heroAttacks() {
        gameWorldService.heroAttacks();
    }

    @Override
    public void onEnemyHit(IEnemy enemy, int damage) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateEnemy(enemy, damage));
    }

    @Override
    public void onEnemyDefeated(IEnemy enemy) {
        // already handled in onEnemyHit
    }

    @Override
    public void onAllEnemiesDefeated() {
        gameWorldVisualizer.handleAllEnemiesDefeated();
    }

    @Override
    public void onHeroHit(IHero hero, ICharacter attackingCharacter, int damage) {
        onHeroTakeDamage(hero, damage);
        logger.log(Level.INFO, "Hero hit by enemy. Damage: " + damage + " Health: " + hero.getHealth());
    }

    @Override
    public void onHeroHit(IHero hero, IWeapon weapon, int damage) {
        onHeroTakeDamage(hero, damage);
    }

    public void onHeroTakeDamage(IHero hero, int damage) {
        // TODO: handle in HeroService
        hero.decreaseHealth(damage);

        if (hero.getHealth() > 0) {
            gameWorldVisualizer.handleHeroHit();
        }
        else {
            gameWorldVisualizer.handleHeroDefeated();
        }
    }

    public void onEnemyHitByBomb(IEnemy enemy, IBomb bomb, int damage) {
        // TODO: handle in EnemyService
        ((EnemyService)gameWorldService.getEnemyService()).onHeroAttacksCharacter(enemy, damage);

        logger.log(Level.INFO, "Enemy hit by bomb. Damage: " + damage + " Health: " + enemy.getHealth());
    }

    @Override
    public void onBombPlaced(IBomb newBomb, Collection<IBomb> allBombs) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.updateBombs(allBombs));
    }

    @Override
    public void onBombExploded(IBomb bomb, List<Point> attackPoints) {
        // GUI operations must be executed on the JavaFX application thread
        logger.info("Bomb exploded" + bomb);
        Platform.runLater(() -> gameWorldVisualizer.bombExploded(bomb, attackPoints));
    }

    @Override
    public void onBombExplosionFinished(IBomb bomb) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.bombExplosionFinished(bomb));
    }

    @Override
    public void onProjectileCreated(IProjectile projectile) {
        if (gameWorldVisualizer == null) {
            return;
        }
        Platform.runLater(() -> gameWorldVisualizer.addProjectile(projectile));
    }

    @Override
    public void onProjectileMoved(IProjectile projectile) {
        if (gameWorldVisualizer == null) {
            return;
        }
        Platform.runLater(() -> gameWorldVisualizer.updateProjectile(projectile));
    }

    @Override
    public void onProjectileFinished(IProjectile projectile) {
        if (gameWorldVisualizer == null) {
            return;
        }
        Platform.runLater(() -> gameWorldVisualizer.removeProjectile(projectile));
    }

    @Override
    public void onTileHit(IWeapon weapon, Tile tile) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.handleTileHit(weapon, tile));
    }

    @Override
    public void onHeroMoved(IHero hero, int oldX, int oldY) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> gameWorldVisualizer.handleHeroMoved(hero, oldX, oldY));
    }

    public void onInventarItemClicked(IItem item) {
        gameWorldService.onInventarItemSelected(item);
    }

    @Override
    public void onItemCollected(ItemCollectedEvent event) {
        onItemPickedUp(event.character(), event.item());
    }

    @Override
    public void onItemEquipped(ItemEquippedEvent event) {

    }

    @Override
    public void onItemAddedToInventory(ItemAddedToInventoryEvent event) {

    }

    @Override
    public void onItemUsed(ItemConsumedEvent event) {

    }
}
