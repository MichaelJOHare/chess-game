package com.michaeljohare.chess.game;

import static com.michaeljohare.chess.game.Board.*;

public class Square {
    private int x;
    private int y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square square = (Square) o;
            return square.x == this.x && square.y == this.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return legendLetter[y] + legendNumber[x];
    }
}
