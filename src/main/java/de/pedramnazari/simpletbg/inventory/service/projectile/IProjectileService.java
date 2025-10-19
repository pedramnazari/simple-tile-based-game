package de.pedramnazari.simpletbg.inventory.service.projectile;

import de.pedramnazari.simpletbg.inventory.service.IWeaponDealsDamageListener;
import de.pedramnazari.simpletbg.tilemap.model.IProjectile;

public interface IProjectileService {
    void launchProjectile(IProjectile projectile);

    void addProjectileEventListener(IProjectileEventListener listener);

    void addWeaponDealsDamageListener(IWeaponDealsDamageListener listener);
}
