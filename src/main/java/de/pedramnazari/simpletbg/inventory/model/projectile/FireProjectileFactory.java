package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileFactory;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

public class FireProjectileFactory implements IProjectileFactory {
    @Override
    public IProjectile createProjectile(int x, int y, MoveDirection direction, int range, IWeapon weapon, int damage) {
        return new FireProjectile(x, y, direction, range, weapon, damage);
    }
}
