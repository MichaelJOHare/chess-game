package com.michaeljohare.chess.GUI;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;
import com.michaeljohare.chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.michaeljohare.chess.game.Board.*;
import static com.michaeljohare.chess.game.Board.board;

public class ChessGUI extends JFrame {

    /*TO DO:
    * Implement pawn promote
    * Implement undo
    * Implement display (available moves, captured pieces)
    * Highlight squares available instead of sout
    * Choosing square not in move list should allow reselect of different piece
    * A-H 1-8 Legend
    * */

    private JTextField display;
    private JButton[][] chessButtons;
    private int turnCounter;
    private boolean hasCastled = false;
    private final Player player1 = new Player(PLAYER_1);
    private final Player player2 = new Player(PLAYER_2);
    private Square sourceSquare;
    private boolean isFirstClick = true;
    private ChessPiece capturedPiece;
    private ChessPiece playerPiece;
    private ChessPiece previousPiece;

    public ChessGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Chess");
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(8, 8));

        chessButtons = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessButtons[row][col] = new JButton();
                updateButton(row, col);
                final int finalRow = row;
                final int finalCol = col;
                chessButtons[row][col].addActionListener(e -> onSquareClick(finalRow, finalCol));
                frame.add(chessButtons[row][col]);
            }
        }
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        updateGUI();
    }

    private void updateButton(int row, int col) {

        if (board[row][col].startsWith(PAWN)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_PAWN);
            } else {
                chessButtons[row][col].setText(BLACK_PAWN);
            }
        }

        if (board[row][col].startsWith(ROOK)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_ROOK);
            } else {
                chessButtons[row][col].setText(BLACK_ROOK);
            }
        }

        if (board[row][col].startsWith(BISHOP)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_BISHOP);
            } else {
                chessButtons[row][col].setText(BLACK_BISHOP);
            }
        }

        if (board[row][col].startsWith(KNIGHT)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_KNIGHT);
            } else {
                chessButtons[row][col].setText(BLACK_KNIGHT);
            }
        }

        if (board[row][col].startsWith(KING)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_KING);
            } else {
                chessButtons[row][col].setText(BLACK_KING);
            }
        }

        if (board[row][col].startsWith(QUEEN)) {
            if (board[row][col].endsWith(PLAYER_1)) {
                chessButtons[row][col].setText(WHITE_QUEEN);
            } else {
                chessButtons[row][col].setText(BLACK_QUEEN);
            }
        }

        if (board[row][col].equals(EMPTY)) {
            chessButtons[row][col].setText(EMPTY);
        }
    }

    private void onSquareClick(int row, int col) {
        if (isFirstClick) {
            if (turnCounter % 2 == 0) {
                playerPiece = player1.getPlayerPiece(new Square(row, col));
            } else if (turnCounter % 2 == 1) {
                playerPiece = player2.getPlayerPiece(new Square(row, col));
            }

            if (playerPiece == null) {
                System.out.println("No piece in square");
            } else {
                sourceSquare = new Square(row, col);
                List<Square> moves = playerPiece.getMoves();
                if (moves.size() == 0) {
                    System.out.println("\nThe piece you selected does not have any legal moves");
                    playerPiece = null;
                    isFirstClick = true;
                }else {
                    System.out.println("\nThese are the available moves");
                    for (Square square : moves) {
                        System.out.println(square);
                    }
                    isFirstClick = false;
                }
            }
        } else {
            Square targetSquare = new Square(row, col);
            if (playerPiece.getMoves().contains(targetSquare)) {
                if (!isEmpty(row, col)) {
                    playerPiece.movePiece(new Square(row, col));
                    if (turnCounter % 2 == 0) {
                        capturedPiece = player2.getPlayerPiece(new Square(row, col));
                        player2.capturePiece(capturedPiece);
                        hasCastled = false;
/*                        if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0) {
                            System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                    "to promote to rook" +
                                    "\n\n1)Rook" +
                                    "\n2)Knight" +
                                    "\n3)Bishop" +
                                    "\n4)Queen" +
                                    "\n\nEnter your choice >>> ");
                            pawnPromote(x, y, player1);
                        }*/
                    } else {
                        capturedPiece = player1.getPlayerPiece(new Square(row, col));
                        player1.capturePiece(capturedPiece);
                        hasCastled = false;
/*                        if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 7) {
                            System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                    "to promote to rook" +
                                    "\n\n1)Rook" +
                                    "\n2)Knight" +
                                    "\n3)Bishop" +
                                    "\n4)Queen" +
                                    "\n\nEnter your choice >>> ");
                            pawnPromote(x, y, player2);
                        }*/
                    }
                } else {
                    capturedPiece = null;
                    playerPiece.movePiece(new Square(row, col));
                    hasCastled = false;
                    if (playerPiece instanceof King) {
                        if (turnCounter % 2 == 0 && row == 7 && col == 6) {
                            player1.getPlayerPiece(new Square(7, 7)).movePiece(new Square(7, 5));
                            ((Rook) player1.getPlayerPiece(new Square(7, 5))).hasMoved = true;
                            hasCastled = true;
                        }
                        if (turnCounter % 2 == 0 && row == 7 && col == 2) {
                            player1.getPlayerPiece(new Square(7, 0)).movePiece(new Square(7, 3));
                            ((Rook) player1.getPlayerPiece(new Square(7, 3))).hasMoved = true;
                            hasCastled = true;
                        }
                        if (turnCounter % 2 == 1 && row == 0 && col == 6) {
                            player2.getPlayerPiece(new Square(0, 7)).movePiece(new Square(0, 5));
                            ((Rook) player2.getPlayerPiece(new Square(0, 5))).hasMoved = true;
                            hasCastled = true;
                        }
                        if (turnCounter % 2 == 1 && row == 0 && col == 2) {
                            player2.getPlayerPiece(new Square(0, 0)).movePiece(new Square(0, 3));
                            ((Rook) player2.getPlayerPiece(new Square(0, 3))).hasMoved = true;
                            hasCastled = true;
                        }
                        ((King) playerPiece).hasMoved = true;
                    }
                    if (playerPiece instanceof Rook) ((Rook) playerPiece).hasMoved = true;
/*                    if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0) {
                        System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                "to promote to rook" +
                                "\n\n1)Rook" +
                                "\n2)Knight" +
                                "\n3)Bishop" +
                                "\n4)Queen" +
                                "\n\nEnter your choice >>> ");
                        pawnPromote(x, y, player1);
                    }
                    if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 7) {
                        System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                "to promote to rook" +
                                "\n\n1)Rook" +
                                "\n2)Knight" +
                                "\n3)Bishop" +
                                "\n4)Queen" +
                                "\n\nEnter your choice >>> ");
                        pawnPromote(x, y, player2);
                    }*/
                }
                updateGUI();
                previousPiece = playerPiece;
                playerPiece = null;
                sourceSquare = null;
                isFirstClick = true;
                turnCounter++;
            }
        }
        // UNDO logic
/*        try {
            if (capturedPiece != null) {
                if (turnCounter % 2 == 0) {
                    playerPiece.undoMovePiece(capturedPiece.getChessPieceConstant() + player1.getPlayer());
                    player1.undoCapturePiece(capturedPiece);
                } else {
                    playerPiece.undoMovePiece(capturedPiece.getChessPieceConstant() + player2.getPlayer());
                    player2.undoCapturePiece(capturedPiece);
                }
            } else {
                playerPiece.undoMovePiece(EMPTY);
                if (playerPiece instanceof King) ((King) playerPiece).hasMoved = false;
                if (playerPiece instanceof Rook) ((Rook) playerPiece).hasMoved = false;
                if (hasCastled) {
                    if (turnCounter % 2 == 1) {
                        if (board[7][5].equals(ROOK + PLAYER_1)) {
                            ((Rook) player1.getPlayerPiece(new Square(7, 5))).hasMoved = false;
                            player1.getPlayerPiece(new Square(7, 5)).undoMovePiece(EMPTY);
                        } else if (board[7][3].equals(ROOK + PLAYER_1)) {
                            ((Rook) player1.getPlayerPiece(new Square(7, 3))).hasMoved = false;
                            player1.getPlayerPiece(new Square(7, 3)).undoMovePiece(EMPTY);
                        }
                    } else {
                        if (board[0][5].equals(ROOK + PLAYER_2)) {
                            ((Rook) player2.getPlayerPiece(new Square(0, 5))).hasMoved = false;
                            player2.getPlayerPiece(new Square(0, 5)).undoMovePiece(EMPTY);
                        } else if (board[0][3].equals(ROOK + PLAYER_2)) {
                            ((Rook) player2.getPlayerPiece(new Square(0, 3))).hasMoved = false;
                            player2.getPlayerPiece(new Square(0, 3)).undoMovePiece(EMPTY);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n\nYou can only undo a previous move one time\n");
            turnCounter++;
        }
        turnCounter--;
        playerPiece = previousPiece;
        */


    }

    public void updateGUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                updateButton(row, col);
            }
        }
    }

    private boolean isEmpty(int x, int y) {
        return board[x][y].equals(EMPTY);
    }

}
