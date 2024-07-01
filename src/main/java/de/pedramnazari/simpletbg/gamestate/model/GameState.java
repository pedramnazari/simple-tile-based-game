package de.pedramnazari.simpletbg.gamestate.model;

public class GameState {
    private int score;

    private boolean gameOver;

    public GameState() {
        this.score = 0;
        this.gameOver = false;
    }

    public int getScore() {
        return score;
    }
    public void
    increaseScore(int amount) {
        score += amount;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
