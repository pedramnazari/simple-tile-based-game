package de.pedramnazari.simpletbg.tilemap.model;

public interface IProjectileFactory {
    IProjectile createProjectile(int x, int y, MoveDirection direction, int range, IWeapon weapon, int damage);
}
