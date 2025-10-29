package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.ArrayList;
import java.util.List;

class HeroStartConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        int heroRow = context.getHeroStartRow();
        int heroColumn = context.getHeroStartColumn();

        if (!context.isWithinBounds(heroRow, heroColumn)) {
            violations.add("Hero start position (" + heroColumn + "," + heroRow + ") is outside of map bounds");
            return violations;
        }

        if (!context.isWalkable(heroRow, heroColumn)) {
            violations.add("Hero start position (" + heroColumn + "," + heroRow + ") must be walkable");
        }

        return violations;
    }
}
