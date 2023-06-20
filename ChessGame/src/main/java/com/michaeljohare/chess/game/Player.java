package com.michaeljohare.chess.game;

import com.michaeljohare.chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String player;
    private ChessPiece[] playerPieces = new ChessPiece[16];

    public Player(String player) {
        this.player = player;
        setPieces();
    }

    private void setPieces() {
        if (player.equals(Board.PLAYER_1)) {
            playerPieces[0] = new Pawn(new Square(6, 0), this);
            playerPieces[1] = new Pawn(new Square(6, 1), this);
            playerPieces[2] = new Pawn(new Square(6, 2), this);
            playerPieces[3] = new Pawn(new Square(6, 3), this);
            playerPieces[4] = new Pawn(new Square(6, 4), this);
            playerPieces[5] = new Pawn(new Square(6, 5), this);
            playerPieces[6] = new Pawn(new Square(6, 6), this);
            playerPieces[7] = new Pawn(new Square(6, 7), this);
            playerPieces[8] = new Rook(new Square(7, 0), this);
            playerPieces[9] = new Knight(new Square(7, 1), this);
            playerPieces[10] = new Bishop(new Square(7, 2), this);
            playerPieces[11] = new Queen(new Square(7, 3), this);
            playerPieces[12] = new King(new Square(7, 4), this);
            playerPieces[13] = new Bishop(new Square(7, 5), this);
            playerPieces[14] = new Knight(new Square(7, 6), this);
            playerPieces[15] = new Rook(new Square(7, 7), this);
        } else {
            playerPieces[0] = new Pawn(new Square(1, 0), this);
            playerPieces[1] = new Pawn(new Square(1, 1), this);
            playerPieces[2] = new Pawn(new Square(1, 2), this);
            playerPieces[3] = new Pawn(new Square(1, 3), this);
            playerPieces[4] = new Pawn(new Square(1, 4), this);
            playerPieces[5] = new Pawn(new Square(1, 5), this);
            playerPieces[6] = new Pawn(new Square(1, 6), this);
            playerPieces[7] = new Pawn(new Square(1, 7), this);
            playerPieces[8] = new Rook(new Square(0, 0), this);
            playerPieces[9] = new Knight(new Square(0, 1), this);
            playerPieces[10] = new Bishop(new Square(0, 2), this);
            playerPieces[11] = new Queen(new Square(0, 3), this);
            playerPieces[12] = new King(new Square(0, 4), this);
            playerPieces[13] = new Bishop(new Square(0, 5), this);
            playerPieces[14] = new Knight(new Square(0, 6), this);
            playerPieces[15] = new Rook(new Square(0, 7), this);
        }
    }

    public ChessPiece getPlayerPiece(Square square) {
        for (ChessPiece playerPiece : playerPieces) {
            if (playerPiece.isAlive() && playerPiece.getCurrentSquare().equals(square)) {
                return playerPiece;
            }
        }
        return null;
    }
    public List<Square> getMoves() {
        List<Square> availableMoves = new ArrayList<>();
        for (ChessPiece playerPiece : playerPieces) {
            if (playerPiece.isAlive()) {
                availableMoves.addAll(playerPiece.getMoves());
            }
        }
        return availableMoves;
    }

    public String getPlayer() {
        return player;
    }
    public King getKing() {
        return (King) playerPieces[12];
    }

    public void capturePiece(ChessPiece piece) {
        for (int i = 0; i < 16; i++) {
            if (playerPieces[i].equals(piece) && playerPieces[i].isAlive()) {
                playerPieces[i].capture();
                break;
            }
        }
    }
    public void undoCapturePiece(ChessPiece piece) {
        for (int i = 0; i < 16; i++) {
            if (!playerPieces[i].isAlive() && playerPieces[i].isAlive()) {
                piece.undoCapture();
                break;
            }
        }
    }

    public void promotePawn(ChessPiece piece) {
        Board.board[piece.getCurrentSquare().getX()][piece.getCurrentSquare().getY()] = piece.getChessPieceConstant() +
                getPlayer();
        for (int i = 0; i < 16; i++) {
            if (playerPieces[i].getCurrentSquare().equals(piece.getCurrentSquare())) {
                playerPieces[i] = piece;
            }
        }
    }
}
