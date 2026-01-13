package de.pedramnazari.simpletbg.drivers.ui.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for InputState to ensure proper input aggregation and movement calculation.
 */
class InputStateTest {

    @Test
    void testNoInputReturnsZeroMovement() {
        InputState inputState = new InputState();
        
        int[] movement = inputState.getDiscreteMovement();
        
        assertEquals(0, movement[0], "dx should be 0 when no keys pressed");
        assertEquals(0, movement[1], "dy should be 0 when no keys pressed");
        assertFalse(inputState.hasMovementInput(), "Should report no movement input");
    }

    @Test
    void testSingleDirectionInput() {
        InputState inputState = new InputState();
        
        // Test right
        inputState.setRight(true);
        int[] movement = inputState.getDiscreteMovement();
        assertEquals(1, movement[0]);
        assertEquals(0, movement[1]);
        assertTrue(inputState.hasMovementInput());
        
        // Reset and test left
        inputState.setRight(false);
        inputState.setLeft(true);
        movement = inputState.getDiscreteMovement();
        assertEquals(-1, movement[0]);
        assertEquals(0, movement[1]);
        
        // Reset and test down
        inputState.setLeft(false);
        inputState.setDown(true);
        movement = inputState.getDiscreteMovement();
        assertEquals(0, movement[0]);
        assertEquals(1, movement[1]);
        
        // Reset and test up
        inputState.setDown(false);
        inputState.setUp(true);
        movement = inputState.getDiscreteMovement();
        assertEquals(0, movement[0]);
        assertEquals(-1, movement[1]);
    }

    @Test
    void testDiagonalInput() {
        InputState inputState = new InputState();
        
        // Test up-right diagonal
        inputState.setUp(true);
        inputState.setRight(true);
        int[] movement = inputState.getDiscreteMovement();
        assertEquals(1, movement[0], "dx should be 1 for right");
        assertEquals(-1, movement[1], "dy should be -1 for up");
        assertTrue(inputState.hasMovementInput());
        
        // Test down-left diagonal
        inputState.setUp(false);
        inputState.setRight(false);
        inputState.setDown(true);
        inputState.setLeft(true);
        movement = inputState.getDiscreteMovement();
        assertEquals(-1, movement[0], "dx should be -1 for left");
        assertEquals(1, movement[1], "dy should be 1 for down");
    }

    @Test
    void testOpposingHorizontalInputsCancelOut() {
        InputState inputState = new InputState();
        
        // Press both left and right
        inputState.setLeft(true);
        inputState.setRight(true);
        
        int[] movement = inputState.getDiscreteMovement();
        assertEquals(0, movement[0], "dx should be 0 when both left and right are pressed");
        // When opposing inputs cancel, there's still "input" registered, just no net movement
        assertTrue(inputState.hasMovementInput(), "Should still report movement input when keys are pressed");
    }

    @Test
    void testOpposingVerticalInputsCancelOut() {
        InputState inputState = new InputState();
        
        // Press both up and down
        inputState.setUp(true);
        inputState.setDown(true);
        
        int[] movement = inputState.getDiscreteMovement();
        assertEquals(0, movement[1], "dy should be 0 when both up and down are pressed");
        // When opposing inputs cancel, there's still "input" registered, just no net movement
        assertTrue(inputState.hasMovementInput(), "Should still report movement input when keys are pressed");
    }

    @Test
    void testMovementVectorNormalization() {
        InputState inputState = new InputState();
        
        // Single direction should have magnitude 1.0
        inputState.setRight(true);
        double[] vector = inputState.getMovementVector();
        assertEquals(1.0, vector[0], 0.001);
        assertEquals(0.0, vector[1], 0.001);
        
        // Diagonal should be normalized to magnitude 1.0
        inputState.setUp(true);
        vector = inputState.getMovementVector();
        
        // For diagonal: dx=1, dy=-1 normalized = (1/sqrt(2), -1/sqrt(2))
        double expectedValue = 1.0 / Math.sqrt(2);
        assertEquals(expectedValue, vector[0], 0.001, "Normalized dx for diagonal");
        assertEquals(-expectedValue, vector[1], 0.001, "Normalized dy for diagonal");
        
        // Check magnitude is approximately 1.0
        double magnitude = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
        assertEquals(1.0, magnitude, 0.001, "Diagonal movement should be normalized to magnitude 1.0");
    }

    @Test
    void testStateTransitions() {
        InputState inputState = new InputState();
        
        // Start with right
        inputState.setRight(true);
        assertTrue(inputState.hasMovementInput());
        
        // Add up for diagonal
        inputState.setUp(true);
        int[] movement = inputState.getDiscreteMovement();
        assertEquals(1, movement[0]);
        assertEquals(-1, movement[1]);
        
        // Release right, should only have up
        inputState.setRight(false);
        movement = inputState.getDiscreteMovement();
        assertEquals(0, movement[0]);
        assertEquals(-1, movement[1]);
        
        // Release up
        inputState.setUp(false);
        assertFalse(inputState.hasMovementInput());
    }
}
