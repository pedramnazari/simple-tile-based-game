package de.pedramnazari.simpletbg.tilemap.config;

import de.pedramnazari.simpletbg.tilemap.config.validation.MapDesignValidator;
import de.pedramnazari.simpletbg.tilemap.config.validation.MapValidationContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GameMapRepository {

    private final List<GameMapSource> sources;
    private final MapDesignValidator validator;
    private volatile List<GameMapDefinition> cachedMaps = List.of();

    public GameMapRepository(List<GameMapSource> sources, MapDesignValidator validator) {
        this.sources = List.copyOf(Objects.requireNonNull(sources, "sources must not be null"));
        this.validator = Objects.requireNonNull(validator, "validator must not be null");
        reload();
    }

    public static GameMapRepository createDefault() {
        return new GameMapRepository(
                List.of(
                        new ProgrammaticGameMapSource(),
                        new MapJsonLoader()
                ),
                MapDesignValidator.createDefault()
        );
    }

    public void reload() {
        Map<String, GameMapDefinition> aggregated = new LinkedHashMap<>();
        for (GameMapSource source : sources) {
            for (GameMapDefinition definition : source.load()) {
                Objects.requireNonNull(definition, "Map source returned null definition");
                validate(definition);
                GameMapDefinition previous = aggregated.put(definition.getId(), definition);
                if (previous != null) {
                    throw new IllegalStateException("Duplicate map id '" + definition.getId() + "'");
                }
            }
        }

        cachedMaps = List.copyOf(aggregated.values());
    }

    public List<GameMapDefinition> getAll() {
        return cachedMaps;
    }

    public GameMapDefinition getDefaultMap() {
        if (cachedMaps.isEmpty()) {
            throw new IllegalStateException("No maps available");
        }
        return cachedMaps.get(0);
    }

    private void validate(GameMapDefinition definition) {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                definition.getMap(),
                definition.getItems(),
                definition.getEnemies()
        );

        validator.validate(context);

        int heroRow = definition.getHeroStartRow();
        int heroColumn = definition.getHeroStartColumn();
        if (!context.isWithinBounds(heroRow, heroColumn)) {
            throw new IllegalStateException("Hero start position (" + heroColumn + "," + heroRow + ") is outside of map bounds for map '"
                    + definition.getId() + "'");
        }

        if (!context.isWalkable(heroRow, heroColumn)) {
            throw new IllegalStateException("Hero start position (" + heroColumn + "," + heroRow + ") must be walkable for map '"
                    + definition.getId() + "'");
        }

    }
}
