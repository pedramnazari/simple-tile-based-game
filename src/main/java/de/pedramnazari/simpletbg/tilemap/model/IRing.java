package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IRing extends IItem {

    void setMagicPower(IMagicEffect magicPower);

    Optional<IMagicEffect> getMagicPower();


    int getAttackingPower();

    void setAttackingPower(int attackingPower);
}
