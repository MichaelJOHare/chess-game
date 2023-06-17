package com.michaeljohare.chess.game;

public class Board {
    public static final String PAWN = "P";
    public static final String ROOK = "R";
    public static final String KNIGHT = "N";
    public static final String BISHOP = "B";
    public static final String QUEEN = "Q";
    public static final String KING = "K";
    public static final String PLAYER_1 = "1";
    public static final String PLAYER_2 = "2";
    public static final String EMPTY = "  ";
    public static String[][] board = new String[8][8];
    public static String[] legendLetter = { "A", "B", "C", "D", "E", "F", "G", "H" };
    public static String[] legendNumber = { "1", "2", "3", "4", "5", "6", "7", "8" };

    public static void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = EMPTY;
            }
        }

        for (int i = 0; i < 8; i++) {
            board[1][i] = PAWN + PLAYER_2;
            board[6][i] = PAWN + PLAYER_1;
            switch (i) {
                case 0:
                    setPiece(i, ROOK);
                    break;

                case 1:
                    setPiece(i, KNIGHT);
                    break;

                case 2:
                    setPiece(i, BISHOP);
            }
        }

        board[0][3] = QUEEN + PLAYER_2;
        board[7][3] = QUEEN + PLAYER_1;
        board[0][4] = KING + PLAYER_2;
        board[7][4] = KING + PLAYER_1;
    }

    private static void setPiece(int i, String piece) {
        board[0][i] = piece + PLAYER_2;
        board[0][7 - i] = piece + PLAYER_2;
        board[7][i] = piece + PLAYER_1;
        board[7][7 - i] = piece + PLAYER_1;
    }

    public static void displayBoard() {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(legendLetter[i] + "|");
            for (int j = 0; j < 8; j++) {
                System.out.print("\t" + "[" + board[i][j] + "]");
            }
            System.out.println();
        }
        for (int j = 1; j < 9; j++) {
            System.out.print("\t" + " " + "__" + "\t");
        }
        System.out.println();
        for (int i = 1; i < 9; i++) {
            System.out.print("\t" + "  " + legendNumber[i-1] + "\t");
        }
        System.out.println("\n\n");
    }
}
