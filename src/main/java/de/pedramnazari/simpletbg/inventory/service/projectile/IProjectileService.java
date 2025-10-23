package de.pedramnazari.simpletbg.inventory.service.projectile;

import de.pedramnazari.simpletbg.inventory.model.projectile.IProjectileLauncher;
import de.pedramnazari.simpletbg.inventory.service.IWeaponDealsDamageListener;

public interface IProjectileService extends IProjectileLauncher {

    void addProjectileEventListener(IProjectileEventListener listener);

    void addWeaponDealsDamageListener(IWeaponDealsDamageListener listener);
}
