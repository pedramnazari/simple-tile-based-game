package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.inventory.service.projectile.IProjectileService;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileFactory;
import de.pedramnazari.simpletbg.tilemap.model.IRangedWeapon;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

public class FireStaff extends Weapon implements IRangedWeapon {

    private final IProjectileFactory projectileFactory;
    private final IProjectileService projectileService;

    public FireStaff(int x,
                     int y,
                     IProjectileFactory projectileFactory,
                     IProjectileService projectileService) {
        super(x, y, "Fire Staff", "A staff that shoots blazing fire.", TileType.WEAPON_FIRE_STAFF.getType());
        this.projectileFactory = projectileFactory;
        this.projectileService = projectileService;
        setRange(5);
        setAttackingDamage(15);
    }

    @Override
    public void shoot(IHero hero, int damage) {
        MoveDirection direction = hero.getMoveDirection().orElse(MoveDirection.RIGHT);
        int startX = hero.getX();
        int startY = hero.getY();

        IProjectile projectile = projectileFactory.createProjectile(startX, startY, direction, getRange(), this, damage);
        projectileService.launchProjectile(projectile);
    }
}
