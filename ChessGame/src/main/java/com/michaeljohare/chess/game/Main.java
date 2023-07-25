package com.michaeljohare.chess.game;

import com.michaeljohare.chess.GUI.ChessGUI;

public class Main {

    public static void main(String[] args) {
        Board.initializeBoard();
        ChessGUI chessGUI = new ChessGUI();

        // Play play = new Play();
/*        while (true) {
            play.play();
            if (!hasWon(play)) {
                Board.displayBoard();
                if (play.getTurnCounter() % 2 == 0) {
                    System.out.println("\nPLAYER 2 HAS WON THE GAME!\n");
                } else {
                    System.out.println("\nPLAYER 1 HAS WON THE GAME!\n");
                    break;
                }
            }
        }*/
    }

    private static boolean hasWon(Play play) {
        return !((play.getTurnCounter() % 2 == 0 && play.getPlayer1().getMoves().size() == 0) ||
                (play.getTurnCounter() % 2 == 1 && play.getPlayer2().getMoves().size() == 0));
    }
}