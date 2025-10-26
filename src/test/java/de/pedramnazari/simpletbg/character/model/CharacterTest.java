package de.pedramnazari.simpletbg.character.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    private TestCharacter character;

    @BeforeEach
    void setUp() {
        character = new TestCharacter(1, 0, 0);
    }

    @Test
    void increaseHealth_negativeValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> character.increaseHealth(-5));
    }

    @Test
    void increaseHealth_byZero_healthDoesNotChange() {
        int initialHealth = character.getHealth();
        int result = character.increaseHealth(0);

        assertEquals(initialHealth, character.getHealth());
        assertEquals(initialHealth, result);
    }

    @Test
    void increaseHealth_positiveValueWithinLimit_healthIncreased() {
        character.decreaseHealth(30);
        int result = character.increaseHealth(20);

        assertEquals(90, character.getHealth());
        assertEquals(90, result);
    }

    @Test
    void increaseHealth_valueExceedingLimit_healthBecomesMaximum() {
        int result = character.increaseHealth(50); // 100 + 50 should become 100

        assertEquals(100, character.getHealth());
        assertEquals(100, result);
    }

    @Test
    void decreaseHealth_negativeValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> character.decreaseHealth(-5));
    }

    @Test
    void decreaseHealth_byZero_healthDoesNotChange() {
        int initialHealth = character.getHealth();
        int result = character.decreaseHealth(0);

        assertEquals(initialHealth, character.getHealth());
        assertEquals(initialHealth, result);
    }

    @Test
    void decreaseHealth_positiveValueWithinLimit_healthDecreased() {
        int result = character.decreaseHealth(30);

        assertEquals(70, character.getHealth());
        assertEquals(70, result);
    }

    @Test
    void decreaseHealth_valueBelowMinimum_healthBecomesZero() {
        int result = character.decreaseHealth(150); // 100 - 150 should become 0

        assertEquals(0, character.getHealth());
        assertEquals(0, result);
    }

    // Concrete test implementation of abstract Character class
    private static class TestCharacter extends Character {
        protected TestCharacter(int type, int x, int y) {
            super(type, x, y);
        }
    }
}

