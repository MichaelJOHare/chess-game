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
    public static String[] legendNumber = { "8", "7", "6", "5", "4", "3", "2", "1" };
    public static final String WHITE_PAWN = "\u2659";
    public static final String WHITE_KNIGHT = "\u2658";
    public static final String WHITE_BISHOP = "\u2657";
    public static final String WHITE_ROOK = "\u2656";
    public static final String WHITE_QUEEN = "\u2655";
    public static final String WHITE_KING = "\u2654";
    public static final String BLACK_PAWN = "\u265F";
    public static final String BLACK_KNIGHT = "\u265E";
    public static final String BLACK_BISHOP = "\u265D";
    public static final String BLACK_ROOK = "\u265C";
    public static final String BLACK_QUEEN = "\u265B";
    public static final String BLACK_KING = "\u265A";


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
            System.out.print(legendNumber[i] + "|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j].startsWith(PAWN)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_PAWN + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_PAWN + "\u2009" + "]");
                    }
                } else if (board[i][j].startsWith(ROOK)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_ROOK + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_ROOK + "\u2009" + "]");
                    }
                } else if (board[i][j].startsWith(BISHOP)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_BISHOP + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_BISHOP + "\u2009" + "]");
                    }
                } else if (board[i][j].startsWith(KNIGHT)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_KNIGHT + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_KNIGHT + "\u2009" + "]");
                    }
                } else if (board[i][j].startsWith(QUEEN)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_QUEEN + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_QUEEN + "\u2009" + "]");
                    }
                } else if (board[i][j].startsWith(KING)) {
                    if (board[i][j].endsWith(PLAYER_2)) {
                        System.out.print("  " + "[" + "\u2009" + WHITE_KING + "\u2009" + "]");
                    } else {
                        System.out.print("  " + "[" + "\u2009" + BLACK_KING + "\u2009" + "]");
                    }
                } else {
                    System.out.print("  " + "[" + board[i][j] + "\u2009" + "]");
                }
            }
            System.out.println();
        }
        System.out.print("\t" + " " + "__");
        for (int j = 1; j < 8; j++) {
            System.out.print("    " + "\u2009" +  "__");
        }
        System.out.println();
        for (int i = 1; i < 9; i++) {
            if (i == 1) {
                System.out.print("\u2005" + "     " + legendLetter[i-1]);
            } else {
                System.out.print("     " + "\u2009" + legendLetter[i-1]);
            }
        }
        System.out.println("\n\n");
    }
}
