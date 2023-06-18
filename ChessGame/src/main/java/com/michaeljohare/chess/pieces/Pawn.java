package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

import static com.michaeljohare.chess.game.Board.PAWN;

public class Pawn extends ChessPiece {
    public Pawn(Square currentSquare, Player player) {
        super(currentSquare, player);
    }

    @Override
    public List<Square> getMoves() {
        return null;
    }



    @Override
    public String getChessPieceConstant() {
        return PAWN;
    }


}
