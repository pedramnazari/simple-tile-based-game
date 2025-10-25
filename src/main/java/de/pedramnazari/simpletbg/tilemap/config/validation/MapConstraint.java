package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.List;

public interface MapConstraint {

    List<String> validate(MapValidationContext context);
}
