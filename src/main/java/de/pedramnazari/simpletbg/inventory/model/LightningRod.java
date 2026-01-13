package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileFactory;
import de.pedramnazari.simpletbg.tilemap.model.IRangedWeapon;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.Optional;

public class LightningRod extends Weapon implements IRangedWeapon {

    private final IProjectileFactory projectileFactory;

    public LightningRod(int x,
                        int y,
                        IProjectileFactory projectileFactory) {
        super(x, y, "Lightning Rod", "A rod that shoots chaining lightning.", TileType.WEAPON_LIGHTNING_ROD.getType());
        this.projectileFactory = projectileFactory;
        setRange(5);
        setAttackingDamage(10);
    }

    @Override
    public Optional<IProjectile> createProjectile(IHero hero, int damage) {
        MoveDirection direction = hero.getMoveDirection().orElse(MoveDirection.RIGHT);
        int startX = hero.getX();
        int startY = hero.getY();

        IProjectile projectile = projectileFactory.createProjectile(startX, startY, direction, getRange(), this, damage);
        return Optional.of(projectile);
    }
}
