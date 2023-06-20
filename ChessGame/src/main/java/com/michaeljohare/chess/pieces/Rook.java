package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

public class Rook extends ChessPiece {

    public boolean hasMoved;

    public Rook(Square currentSquare, Player player) {
        super(currentSquare, player);
        hasMoved = false;
    }

    public boolean isAbleToCastle() {
        return hasMoved;
    }

    @Override
    public List<Square> getMoves() {
        return null;
    }

    @Override
    public String getChessPieceConstant() {
        return null;
    }
}
