package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;

public interface IHeroAttackListener {

    void onHeroAttacksCharacter(Enemy enemy, int damage);
}
