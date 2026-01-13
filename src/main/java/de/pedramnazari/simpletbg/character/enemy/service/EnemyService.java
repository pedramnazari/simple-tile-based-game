package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.service.IWeaponDealsDamageListener;
import de.pedramnazari.simpletbg.inventory.service.event.*;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.HeroHitNotifier;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.IHeroHitListener;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyService implements IEnemyService, IEnemySubject, IHeroAttackListener, IWeaponDealsDamageListener {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final HeroHitNotifier heroHitNotifier = new HeroHitNotifier();

    private final Collection<IItemEventListener> itemEventListeners = new ArrayList<>();

    private final EnemyMovementService enemyMovementService;

    private final Collection<IEnemy> enemies = new ArrayList<>();
    private final List<IEnemy> enemiesToAdd = new ArrayList<>();
    private boolean initialized = false;

    private final List<IEnemyObserver> observers = new ArrayList<>();
    final EnemyHitNotifier enemyHitNotifier = new EnemyHitNotifier();

    public EnemyService(EnemyMovementService enemyMovementService) {
        this.enemyMovementService = enemyMovementService;
    }

    @Override
    public void init(Collection<IEnemy> enemies) {
        if (initialized) {
            throw new IllegalStateException("Enemies already initialized");
        }

        this.enemies.addAll(enemies);
        initialized = true;
    }

    @Override
    public List<MovementResult> moveEnemies(final GameContext gameContext) {
        final List<MovementResult> movementResults = new ArrayList<>();

        // Add any newly spawned enemies to the main collection
        if (!enemiesToAdd.isEmpty()) {
            enemies.addAll(enemiesToAdd);
            enemiesToAdd.clear();
            notifyObservers();
        }

        for (IEnemy enemy : enemies) {
            // Check for summoner spawn logic
            if (enemy instanceof de.pedramnazari.simpletbg.character.enemy.model.SummonerEnemy summoner) {
                if (summoner.shouldSpawn()) {
                    spawnRushCreature(gameContext, summoner);
                }
            }

            // Check if enemy is frozen
            if (enemy.getFrozenTurns() > 0) {
                enemy.decrementFrozenTurns();
                // Enemy skips its turn - create an empty movement result
                MovementResult frozenResult = new MovementResult();
                frozenResult.setOldX(enemy.getX());
                frozenResult.setOldY(enemy.getY());
                frozenResult.setNewX(enemy.getX());
                frozenResult.setNewY(enemy.getY());
                frozenResult.setHasElementMoved(false);
                movementResults.add(frozenResult);
                continue;
            }

            final Point newPosition = enemyMovementService.calcNextMove(gameContext.getTileMap(), enemy);

            final MovementResult result = enemyMovementService
                    .moveElementToPositionWithinMap(gameContext, enemy, newPosition.getX(), newPosition.getY());

            handleItemIfCollected(result);

            if (!result.getCollidingElements().isEmpty()) {
                // TODO: Move to HeroService (or CollisionService)
                // Assumption for now:
                // Enemies do not collide with each other.
                // Enemies can only collide with the hero.
                // So the colliding element is always the hero.
                final IHero hero = (IHero) result.getCollidingElements().iterator().next();

                heroHitNotifier.notifyHeroHit(hero, enemy, enemy.getAttackingPower());

            }

            movementResults.add(result);
        }

        if (!movementResults.isEmpty()) {
            notifyObservers();
        }

        return movementResults;
    }

    @Override
    public List<MovementResult> moveRushCreatures(final GameContext gameContext) {
        final List<MovementResult> movementResults = new ArrayList<>();

        for (IEnemy enemy : enemies) {
            // Only move rush creatures
            if (enemy.getType() != TileType.ENEMY_RUSH_CREATURE.getType()) {
                continue;
            }

            // Check if enemy is frozen
            if (enemy.getFrozenTurns() > 0) {
                enemy.decrementFrozenTurns(); // Decrement frozen counter
                continue; // Skip frozen rush creatures
            }

            final Point newPosition = enemyMovementService.calcNextMove(gameContext.getTileMap(), enemy);

            final MovementResult result = enemyMovementService
                    .moveElementToPositionWithinMap(gameContext, enemy, newPosition.getX(), newPosition.getY());

            handleItemIfCollected(result);

            if (!result.getCollidingElements().isEmpty()) {
                final IHero hero = (IHero) result.getCollidingElements().iterator().next();
                heroHitNotifier.notifyHeroHit(hero, enemy, enemy.getAttackingPower());
            }

            movementResults.add(result);
        }

        if (!movementResults.isEmpty()) {
            notifyObservers();
        }

        return movementResults;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final IItem item = result.getCollectedItem().get();

            notifyItemCollected(new ItemCollectedEvent(result.getItemCollector().get(), item));
        }
    }

    @Override
    public Collection<IEnemy> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void registerObserver(IEnemyObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IEnemyObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // TODO: currently, we notify all observers about all enemies
        //       In future, we could notify only about the enemies that have changed
        for (IEnemyObserver observer : observers) {
            observer.update(List.copyOf(enemies));
        }
    }

    public void addEnemyHitListener(IEnemyHitListener listener) {
        enemyHitNotifier.addListener(listener);
    }

    @Override
    public void onHeroAttacksCharacter(final ICharacter attackCharacter, int damage) {
        if (!(attackCharacter instanceof IEnemy enemy)) {
            return;
        }

        final int newHealth = enemy.decreaseHealth(damage);

        logger.log(Level.INFO, "Hero attacks enemy. Health: {0}", enemy.getHealth());

        enemyHitNotifier.notifyEnemyHit(enemy, damage);

        if (newHealth == 0) {
            enemies.remove(enemy);

            logger.log(Level.INFO, "Enemy defeated. Remaining enemies: {0}", enemies.size());

            enemyHitNotifier.notifyEnemyDefeated(enemy);

            if (enemies.isEmpty()) {
                enemyHitNotifier.notifyAllEnemiesDefeated();
            }
        }

    }

    public void addHeroHitListener(IHeroHitListener listener) {
        heroHitNotifier.addListener(listener);
    }

    @Override
    public void onWeaponDealsDamage(IWeapon weapon, ICharacter attackedCharacter, int damage) {
        onHeroAttacksCharacter(attackedCharacter, damage);
    }

    public void addItemEventListener(IItemEventListener listener) {
        itemEventListeners.add(listener);
    }

    private void notifyItemCollected(ItemCollectedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemCollected(event);
        }
    }

    private void notifyItemEquipped(ItemEquippedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemEquipped(event);
        }
    }

    private void notifyItemAddedToInventory(ItemAddedToInventoryEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemAddedToInventory(event);
        }
    }

    private void notifyItemConsumed(ItemConsumedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemUsed(event);
        }
    }

    private void spawnRushCreature(GameContext gameContext, de.pedramnazari.simpletbg.character.enemy.model.SummonerEnemy summoner) {
        // Try to spawn adjacent to summoner
        int summonerX = summoner.getX();
        int summonerY = summoner.getY();
        
        // Try all four directions to find a valid spawn position
        int[][] offsets = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // up, down, left, right
        
        for (int[] offset : offsets) {
            int spawnX = summonerX + offset[0];
            int spawnY = summonerY + offset[1];
            
            // Check if position is valid
            if (isValidSpawnPosition(gameContext, spawnX, spawnY)) {
                de.pedramnazari.simpletbg.character.enemy.model.RushCreature rushCreature = 
                    new de.pedramnazari.simpletbg.character.enemy.model.RushCreature(
                        TileType.ENEMY_RUSH_CREATURE.getType(), spawnX, spawnY);
                
                // Create a character provider for the hero
                de.pedramnazari.simpletbg.tilemap.model.ICharacterProvider<de.pedramnazari.simpletbg.tilemap.model.ICharacter> heroProvider = 
                    () -> gameContext.getHero();
                
                // Set up the rush creature with unique movement strategy
                rushCreature.setMovementStrategy(
                    new de.pedramnazari.simpletbg.tilemap.service.navigation.RushCreatureMovementStrategy(
                        new de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService(),
                        heroProvider,
                        summoner.getNextSpawnSeed()
                    )
                );
                rushCreature.setAttackingPower(5);
                
                enemiesToAdd.add(rushCreature);
                
                logger.log(Level.INFO, "Summoner spawned rush creature at position: {0}, {1}", 
                    new Object[]{spawnX, spawnY});
                break; // Only spawn one per turn
            }
        }
    }

    private boolean isValidSpawnPosition(GameContext gameContext, int x, int y) {
        TileMap tileMap = gameContext.getTileMap();
        
        // Check bounds
        if (x < 0 || x >= tileMap.getWidth() || y < 0 || y >= tileMap.getHeight()) {
            return false;
        }
        
        // Check for obstacles
        de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService collisionService = 
            new de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService();
        if (collisionService.isCollisionWithObstacle(tileMap, x, y)) {
            return false;
        }
        
        // Check if position is occupied by hero
        IHero hero = gameContext.getHero();
        if (hero.getX() == x && hero.getY() == y) {
            return false;
        }
        
        // Check if position is occupied by another enemy
        for (IEnemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return false;
            }
        }
        
        return true;
    }
}
