package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

import static com.michaeljohare.chess.game.Board.EMPTY;
import static com.michaeljohare.chess.game.Board.board;

public abstract class ChessPiece {
    protected Square lastSquare, currentSquare;
    protected Player player;
    private boolean isAlive;

    public ChessPiece(Square currentSquare, Player player) {
        this.lastSquare = null;
        this.currentSquare = currentSquare;
        this.player = player;
        isAlive = true;
    }

    public abstract List<Square> getMoves();
    public abstract String getChessPieceConstant();

    public void moviePiece(Square end) {
        board[end.getX()][end.getY()] = getChessPieceConstant() + player.getPlayer();
        board[currentSquare.getX()][currentSquare.getY()] = EMPTY;
        lastSquare = currentSquare;
        currentSquare = end;
    }
    public void undoMoviePiece(String piece) {
        board[lastSquare.getX()][lastSquare.getY()] = getChessPieceConstant() + player.getPlayer();
        board[currentSquare.getX()][currentSquare.getY()] = piece;
        currentSquare = lastSquare;
        lastSquare = null;
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public boolean isEmpty(int x, int y) {
        return board[x][y].equals(EMPTY);
    }

    public boolean isAlive() {
        return isAlive;
    }
    public void capture() {
        isAlive = false;
    }
    public void undoCapture() {
        isAlive = true;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ChessPiece) {
            ChessPiece piece = (ChessPiece) o;
            return piece.lastSquare == this.lastSquare && piece.currentSquare == this.currentSquare;
        }
        return false;
    }
    @Override
    public String toString() {
        return getClass().getTypeName() + "at" + currentSquare;
    }
}
