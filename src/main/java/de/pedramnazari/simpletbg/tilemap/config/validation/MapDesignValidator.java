package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.List;

public class MapDesignValidator {

    private final List<MapConstraint> constraints;

    public MapDesignValidator(List<MapConstraint> constraints) {
        this.constraints = List.copyOf(constraints);
    }

    public static MapDesignValidator createDefault() {
        return new MapDesignValidator(List.of(
                new PortalConstraint(),
                new ExitConstraint(),
                new ElementOverlapConstraint(),
                new HorizontalEnemyMovementConstraint(),
                new VerticalEnemyMovementConstraint()
        ));
    }

    public void validate(MapValidationContext context) {
        for (MapConstraint constraint : constraints) {
            constraint.validate(context);
        }
    }
}
