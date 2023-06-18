package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

public class King extends ChessPiece {

    public boolean hasMoved;

    public King(Square currentSquare, Player player) {
        super(currentSquare, player);
        hasMoved = false;
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
