package com.michaeljohare.chess.pieces;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;

import java.util.List;

import static com.michaeljohare.chess.game.Board.*;

public class King extends ChessPiece {

    public boolean hasMoved;

    public King(Square currentSquare, Player player) {
        super(currentSquare, player);
        hasMoved = false;
    }


    public boolean isInCheck() {
        int x = currentSquare.getX(), y = currentSquare.getY();

        // Diagonal (Queen & Bishop) Checker
        while (x < 7 && y < 7 && isEmpty(x + 1, y + 1)) {
            x++;
            y++;
        }

        if ((x < 7 && y < 7 && !player.getPlayer().equals(board[x + 1][y + 1].substring(1)))
                && (board[x + 1][y + 1].startsWith(QUEEN) || board[x + 1][y + 1].startsWith(BISHOP))) {
            return true;
        }

        x = currentSquare.getX();
        y = currentSquare.getY();

        while (x < 7 && y > 0 && isEmpty(x + 1, y - 1)) {
            x++;
            y--;
        }

        if ((x < 7 && y > 0 && !player.getPlayer().equals(board[x + 1][y - 1].substring(1)))
                && (board[x + 1][y - 1].startsWith(QUEEN) || board[x + 1][y - 1].startsWith(BISHOP))) {
            return true;
        }

        x = currentSquare.getX();
        y = currentSquare.getY();

        while (x > 0 && y > 0 && isEmpty(x - 1, y - 1)) {
            x--;
            y--;
        }

        if ((x > 0 && y > 0 && !player.getPlayer().equals(board[x - 1][y - 1].substring(1)))
                && (board[x - 1][y - 1].startsWith(QUEEN) || board[x - 1][y - 1].startsWith(BISHOP))) {
            return true;
        }

        x = currentSquare.getX();
        y = currentSquare.getY();

        while (x > 0 && y < 7 && isEmpty(x - 1, y + 1)) {
            x--;
            y++;
        }

        if ((x > 0 && y < 7 && !player.getPlayer().equals(board[x - 1][y + 1].substring(1)))
                && (board[x - 1][y + 1].startsWith(QUEEN) || board[x - 1][y + 1].startsWith(BISHOP))) {
            return true;
        }

        x = currentSquare.getX();
        y = currentSquare.getY();


        // Straight line (Queen & Rook) Checker
        while (x < 7 && isEmpty(x + 1, y)) {
            x++;
        }

        if ((x < 7 && !player.getPlayer().equals(board[x + 1][y].substring(1)))
                && (board[x + 1][y].startsWith(QUEEN) || board[x + 1][y].startsWith(ROOK))) {
            return true;
        }

        x = currentSquare.getX();

        while (x > 0 && isEmpty(x - 1, y)) {
            x--;
        }

        if ((x > 0 && !player.getPlayer().equals(board[x - 1][y].substring(1)))
                && (board[x - 1][y].startsWith(QUEEN) || board[x - 1][y].startsWith(ROOK))) {
            return true;
        }

        x = currentSquare.getX();

        while (y < 7 && isEmpty(x, y + 1)) {
            y++;
        }

        if ((y < 7 && !player.getPlayer().equals(board[x][y + 1].substring(1)))
                && (board[x][y + 1].startsWith(QUEEN) || board[x][y + 1].startsWith(ROOK))) {
            return true;
        }

        y = currentSquare.getY();

        while (y > 0 && isEmpty(x, y - 1)) {
            y--;
        }

        if ((y > 0 && !player.getPlayer().equals(board[x][y - 1].substring(1)))
                && (board[x][y - 1].startsWith(QUEEN) || board[x][y - 1].startsWith(ROOK))) {
            return true;
        }

        x = currentSquare.getX();
        y = currentSquare.getY();


        // L shape (Knight) Checker




        // 1 square padded box (Opponent King) Checker




        // Directly diagonal (Pawn) Checker
        if (player.getPlayer().equals(PLAYER_1)) {
            if (x > 0 && y > 0 && !player.getPlayer().equals(board[x-1][y-1].substring(1))
                    && board[x-1][y-1].startsWith(PAWN)) {
                return true;
            }
            return (x > 0 && y < 7 && !player.getPlayer().equals(board[x-1][y-1].substring(1))
                    && board[x-1][y-1].startsWith(PAWN));
        } else {
            if (x < 7 && y > 0 && !player.getPlayer().equals(board[x+1][y-1].substring(1))
                    && board[x+1][y-1].startsWith(PAWN)) {
                return true;
            }
            return (x < 7 && y < 7 && !player.getPlayer().equals(board[x+1][y-1].substring(1))
                    && board[x+1][y-1].startsWith(PAWN));
        }

    }

    @Override
    public List<Square> getMoves() {
        return null;
    }

    @Override
    public String getChessPieceConstant() {
        return KING;
    }
}
