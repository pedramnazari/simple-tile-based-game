package de.pedramnazari.simpletbg.inventory.service.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;

public interface IProjectileEventListener {
    void onProjectileCreated(IProjectile projectile);

    void onProjectileMoved(IProjectile projectile);

    void onProjectileFinished(IProjectile projectile);
}
