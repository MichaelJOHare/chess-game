package com.michaeljohare.chess.GUI;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;
import com.michaeljohare.chess.pieces.*;
import com.sun.jdi.BooleanType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.michaeljohare.chess.game.Board.*;
import static com.michaeljohare.chess.game.Board.board;

public class ChessGUI extends JFrame {

    /*TO DO:
    * Implement pawn promote
    * Implement undo
    * Implement check/checkmate
    * Implement message display
    * A-H 1-8 Legend
    * */

    private JButton[][] chessButtons;
    private JTextArea logTextArea;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private int turnCounter;
    private boolean hasCastled = false;
    private final Player player1 = new Player(PLAYER_1);
    private final Player player2 = new Player(PLAYER_2);
    private Square sourceSquare;
    private boolean isFirstClick = true;
    private ChessPiece capturedPiece;
    private List<ChessPiece> player1CapturedPieces = new ArrayList<>();
    private List<ChessPiece> player2CapturedPieces = new ArrayList<>();
    private List<Square> highlightedSquares = new ArrayList<>();
    private ChessPiece playerPiece;
    private ChessPiece previousPiece;

    public ChessGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Chess");
        frame.setSize(1100, 1000);
        frame.setLayout(new BorderLayout());

        JPanel chessboardPanel = new JPanel(new GridLayout(8, 8));
        chessButtons = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessButtons[row][col] = new JButton();
                chessButtons[row][col].setFont(new Font("Roboto", Font.PLAIN, 40));
                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        chessButtons[row][col].setBackground(new Color(248, 240, 198));
                    } else {
                        chessButtons[row][col].setBackground(new Color(156, 98, 69));
                    }
                } else {
                    if (col % 2 == 1) {
                        chessButtons[row][col].setBackground(new Color(248, 240, 198));
                    } else {
                        chessButtons[row][col].setBackground(new Color(156, 98, 69));
                    }
                }
                updateButton(row, col);
                final int finalRow = row;
                final int finalCol = col;
                chessButtons[row][col].addActionListener(e -> onSquareClick(finalRow, finalCol));
                chessboardPanel.add(chessButtons[row][col]);
            }
        }

        logTextArea = new JTextArea(5, 20);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);

        player1CapturedArea = new JTextArea(15, 8);
        player2CapturedArea = new JTextArea(15, 8);
        JLabel capturedPieces1Title = new JLabel("Captured Pieces");
        capturedPieces1Title.setFont(new Font("Roboto", Font.BOLD, 24));
        JLabel capturedPieces2Title = new JLabel("Captured Pieces");
        capturedPieces2Title.setFont(new Font("Roboto", Font.BOLD, 24));
        player1CapturedArea.add(capturedPieces1Title);
        player2CapturedArea.add(capturedPieces2Title);
        player1CapturedArea.setEditable(false);
        player2CapturedArea.setEditable(false);
        player1CapturedArea.setLayout(new FlowLayout());
        player2CapturedArea.setLayout(new FlowLayout());
        player1CapturedArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        player2CapturedArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(player1CapturedArea, BorderLayout.SOUTH);
        rightPanel.add(logScrollPane, BorderLayout.CENTER);
        rightPanel.add(player2CapturedArea, BorderLayout.NORTH);

        frame.add(chessboardPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        updateGUI();
    }

    private void updateButton(int row, int col) {
        String pieceType = getPieceType(board[row][col]);
        String player = getPlayer(board[row][col]);

        if (!pieceType.equals(EMPTY)) {
            String imagePath = getImagePath(pieceType, player);
            try {
                Image pieceImage = ImageIO.read(ChessGUI.class.getResource(imagePath));
                chessButtons[row][col].setIcon(new ImageIcon(pieceImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            chessButtons[row][col].setIcon(null);
        }
    }

    private String getPieceType(String square) {
        if (square.startsWith(PAWN)) {
            return "Pawn";
        } else if (square.startsWith(ROOK)) {
            return "Rook";
        } else if (square.startsWith(BISHOP)) {
            return "Bishop";
        } else if (square.startsWith(KNIGHT)) {
            return "Knight";
        } else if (square.startsWith(KING)) {
            return "King";
        } else if (square.startsWith(QUEEN)) {
            return "Queen";
        } else {
            return EMPTY;
        }
    }

    private String getPlayer(String square) {
        if (square.endsWith(PLAYER_1)) {
            return PLAYER_1;
        } else if (square.endsWith(PLAYER_2)) {
            return PLAYER_2;
        } else {
            return EMPTY;
        }
    }

    private String getImagePath(String pieceType, String player) {
        return "/" + (player.equals(PLAYER_1) ? "White_" : "Black_") + pieceType + ".png";
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
                    for (Square square : moves) {
                        chessButtons[square.getX()][square.getY()].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                        highlightedSquares.add(square);
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
                        player1CapturedPieces.add(capturedPiece);
                        updateCapturedPiecesDisplay();
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
                        player2CapturedPieces.add(capturedPiece);
                        updateCapturedPiecesDisplay();
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
                clearHighlightedSquares();
                previousPiece = playerPiece;
                playerPiece = null;
                sourceSquare = null;
                isFirstClick = true;
                turnCounter++;
            } else {
                System.out.println("The square you chose to move to is not a legal move, choose a piece and try again.");
                clearHighlightedSquares();
                playerPiece = null;
                sourceSquare = null;
                isFirstClick = true;
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

    private void updateCapturedPiecesDisplay() {
        player1CapturedArea.removeAll();
        player2CapturedArea.removeAll();

        for (ChessPiece piece : player1CapturedPieces) {
            JLabel blackCapturedPieceLabel = new JLabel(piece.getBlackChessPieceSymbol());
            blackCapturedPieceLabel.setFont(new Font("Roboto", Font.PLAIN, 26));
            player1CapturedArea.add(blackCapturedPieceLabel);
        }

        for (ChessPiece piece : player2CapturedPieces) {
            JLabel whiteCapturedPieceLabel = new JLabel(piece.getWhiteChessPieceSymbol());
            whiteCapturedPieceLabel.setFont(new Font("Roboto", Font.PLAIN, 26));
            player2CapturedArea.add(whiteCapturedPieceLabel);
        }

        player1CapturedArea.revalidate();
        player1CapturedArea.repaint();
        player2CapturedArea.revalidate();
        player2CapturedArea.repaint();
    }

    private boolean isEmpty(int x, int y) {
        return board[x][y].equals(EMPTY);
    }

    private void clearHighlightedSquares() {
        for (Square square : highlightedSquares) {
            chessButtons[square.getX()][square.getY()].setBorder(null);
        }
        highlightedSquares.clear();
    }
}
