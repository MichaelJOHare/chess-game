package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

public class Knight extends ChessPiece {


    public Knight(Square currentSquare, Player player) {
        super(currentSquare, player);
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
