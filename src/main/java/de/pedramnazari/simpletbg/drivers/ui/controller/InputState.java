package de.pedramnazari.simpletbg.drivers.ui.controller;

/**
 * Aggregates keyboard input state for hero movement.
 * Ensures only one movement command is executed per tick.
 */
public class InputState {
    private boolean right;
    private boolean left;
    private boolean down;
    private boolean up;

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }

    /**
     * Calculate normalized movement direction from input state.
     * Returns {dx, dy} where magnitude is at most 1.0 for consistent speed.
     * Opposing directions cancel out.
     */
    public double[] getMovementVector() {
        int dx = 0;
        int dy = 0;

        // Handle horizontal - opposing directions cancel
        if (right && !left) dx = 1;
        else if (left && !right) dx = -1;

        // Handle vertical - opposing directions cancel
        if (down && !up) dy = 1;
        else if (up && !down) dy = -1;

        // Normalize diagonal movement to maintain consistent speed
        if (dx != 0 && dy != 0) {
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            return new double[] { dx / magnitude, dy / magnitude };
        }

        return new double[] { dx, dy };
    }

    /**
     * Check if any movement key is pressed
     */
    public boolean hasMovementInput() {
        return right || left || down || up;
    }

    /**
     * Get the primary movement direction as discrete tile coordinates.
     * For diagonal movement, returns both dx and dy.
     * Opposing directions cancel out.
     */
    public int[] getDiscreteMovement() {
        int dx = 0;
        int dy = 0;

        // Handle horizontal - opposing directions cancel
        if (right && !left) dx = 1;
        else if (left && !right) dx = -1;

        // Handle vertical - opposing directions cancel
        if (down && !up) dy = 1;
        else if (up && !down) dy = -1;

        return new int[] { dx, dy };
    }
}
