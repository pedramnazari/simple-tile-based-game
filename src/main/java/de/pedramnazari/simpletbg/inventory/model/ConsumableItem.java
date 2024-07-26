package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IConsumableItem;
import de.pedramnazari.simpletbg.tilemap.model.IMagicEffect;

public class ConsumableItem extends Item implements IConsumableItem {

    private final IMagicEffect magicPower;

    public ConsumableItem(IMagicEffect magicPower, int x, int y, int type) {
        super(x, y, "Magic Potion", "Magic Potion", type);
        this.magicPower = magicPower;
    }


    @Override
    public void consume(ICharacter character) {
        magicPower.apply(character);
    }
}
