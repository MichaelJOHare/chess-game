package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

public abstract class ChessPiece {
    protected Square lastPoint, currentPoint;
    protected Player player;
    private boolean isAlive;

    public ChessPiece(Square currentPoint, Player player) {
        this.lastPoint = null;
        this.currentPoint = currentPoint;
        this.player = player;
        isAlive = true;
    }
}
