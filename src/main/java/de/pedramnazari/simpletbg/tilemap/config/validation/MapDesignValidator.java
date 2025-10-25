package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.ArrayList;
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
        List<String> violations = new ArrayList<>();
        for (MapConstraint constraint : constraints) {
            violations.addAll(constraint.validate(context));
        }

        if (!violations.isEmpty()) {
            throw new IllegalStateException("Map validation failed:\n - " + String.join("\n - ", violations));
        }
    }
}
