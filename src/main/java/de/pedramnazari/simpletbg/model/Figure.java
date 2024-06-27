package de.pedramnazari.simpletbg.model;

public abstract class Figure implements IMoveableTileElement {

    private int x;
    private int y;


    protected Figure(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected Figure() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }



}
