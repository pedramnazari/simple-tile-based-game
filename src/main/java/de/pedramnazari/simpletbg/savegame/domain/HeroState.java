package de.pedramnazari.simpletbg.savegame.domain;

import java.util.Objects;
import java.util.Optional;

public record HeroState(int x, int y, int health, int attackingPower, Integer weaponType, Integer ringType) {

    public HeroState {
        if (health < 0) {
            throw new IllegalArgumentException("health must be >= 0");
        }
        if (attackingPower < 0) {
            throw new IllegalArgumentException("attackingPower must be >= 0");
        }
    }

    public Optional<Integer> weaponType() {
        return Optional.ofNullable(weaponType);
    }

    public Optional<Integer> ringType() {
        return Optional.ofNullable(ringType);
    }
}
