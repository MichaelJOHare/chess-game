package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

public class Queen extends ChessPiece {


    public Queen(Square currentSquare, Player player) {
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
