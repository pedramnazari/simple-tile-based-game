package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IRangedWeapon extends IWeapon {
    Optional<IProjectile> createProjectile(IHero hero, int damage);
}
