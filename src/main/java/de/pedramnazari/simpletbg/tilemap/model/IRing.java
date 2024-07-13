package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IRing extends IItem {

    void setMagicPower(IMagicPower magicPower);

    Optional<IMagicPower> getMagicPower();


    int getAttackingPower();

    void setAttackingPower(int attackingPower);
}
