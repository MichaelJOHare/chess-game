package com.michaeljohare.chess.GUI;

import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;
import com.michaeljohare.chess.pieces.*;

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
    * Implement check/checkmate
    * A-H 1-8 Legend
    * fix capturedPiecesTitle disappearing after piece capture
    * */

    private JButton[][] chessButtons;
    private JButton undoButton;
    private JButton playAgainButton;
    private JTextArea logTextArea;
    private final String lineBreaks = "\n\n\n\n\n\n\n";
    private JScrollPane logScrollPane;
    private JLabel capturedPiecesTitle;
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
        updateFrame(frame);
        frame.setSize(1150, 1000);
        frame.setLayout(new BorderLayout());

        JPanel chessboardPanel = createChessboardPanel();

        logTextArea = createLogTextArea();
        logTextArea.setFont(new Font("Roboto", Font.PLAIN, 20));
        logTextArea.setText("\n\n\n Welcome to Michael's Chess Game! \n Use the undo button to undo a \n previous move. " +
                "\n\n It is White's turn to move first.");
        logScrollPane = new JScrollPane(logTextArea);

        player1CapturedArea = createCapturedArea();
        player2CapturedArea = createCapturedArea();

        JPanel rightPanel = createRightPanel();

        frame.add(chessboardPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        updateGUI();
    }

    private JPanel createChessboardPanel() {
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
        return chessboardPanel;
    }

    private void updateFrame(JFrame frame) {
        try {
            Image frameIcon = ImageIO.read(ChessGUI.class.getResource("/Frame_Icon.png"));
            frame.setIconImage(frameIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JTextArea createLogTextArea() {
        JTextArea logTextArea = new JTextArea(5, 20);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);
        return logTextArea;
    }

    private JTextArea createCapturedArea() {
        JTextArea capturedArea = new JTextArea(15, 8);
        capturedPiecesTitle = new JLabel("Captured Pieces");
        capturedPiecesTitle.setFont(new Font("Roboto", Font.BOLD, 24));

        capturedArea.add(capturedPiecesTitle);
        capturedArea.setEditable(false);
        capturedArea.setLayout(new FlowLayout());
        capturedArea.setLineWrap(true);
        capturedArea.setWrapStyleWord(true);
        capturedArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return capturedArea;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        playAgainButton = new JButton("Play Again");
        playAgainButton.setBackground(Color.BLACK);
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setFont(new Font("Roboto", Font.BOLD, 24));

        undoButton = new JButton("Undo");
        undoButton.setBackground(Color.BLACK);
        undoButton.setForeground(Color.WHITE);
        undoButton.setFont(new Font("Roboto", Font.BOLD, 24));
        undoButton.addActionListener(e -> onUndoButtonClick());

        JPanel playAgainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playAgainButtonPanel.add(playAgainButton);

        JPanel undoButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        undoButtonPanel.add(undoButton);

        JPanel logPanelWithButtons = new JPanel(new BorderLayout());
        logPanelWithButtons.add(logScrollPane, BorderLayout.CENTER);
        logPanelWithButtons.add(playAgainButtonPanel, BorderLayout.NORTH);
        logPanelWithButtons.add(undoButtonPanel, BorderLayout.SOUTH);

        rightPanel.add(player1CapturedArea, BorderLayout.SOUTH);
        rightPanel.add(logPanelWithButtons, BorderLayout.CENTER);
        rightPanel.add(player2CapturedArea, BorderLayout.NORTH);

        return rightPanel;
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
                logTextArea.setText(lineBreaks + " There's no piece available in the selected square");
            } else {
                sourceSquare = new Square(row, col);
                List<Square> moves = playerPiece.getMoves();
                if (moves.size() == 0) {
                    System.out.println(lineBreaks + " The piece you selected does not have any legal moves");
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
                logTextArea.removeAll();
                if (turnCounter % 2 == 0) {
                    logTextArea.setText(lineBreaks + " It's the white player's turn to move.");
                } else if (turnCounter % 2 == 1) {
                    logTextArea.setText(lineBreaks+ " It's the black player's turn to move.");
                }
            } else {
                logTextArea.setText(lineBreaks + " The square you chose to move to is not a legal move, choose a " +
                        "piece and try again.");
                clearHighlightedSquares();
                playerPiece = null;
                sourceSquare = null;
                isFirstClick = true;
            }
        }
    }

    private void onUndoButtonClick() {
        if (turnCounter > 0) {
            try {
                if (capturedPiece != null) {
                    if (turnCounter % 2 == 0) {
                        previousPiece.undoMovePiece(capturedPiece.getChessPieceConstant() + player1.getPlayer());
                        player1.undoCapturePiece(capturedPiece);
                    } else {
                        previousPiece.undoMovePiece(capturedPiece.getChessPieceConstant() + player2.getPlayer());
                        player2.undoCapturePiece(capturedPiece);
                    }
                } else {
                    previousPiece.undoMovePiece(EMPTY);
                    if (playerPiece instanceof King) ((King) previousPiece).hasMoved = false;
                    if (playerPiece instanceof Rook) ((Rook) previousPiece).hasMoved = false;
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
                logTextArea.setText(lineBreaks + " You can only undo a previous move \n one time!");
                turnCounter++;
            }
            updateGUI();
            turnCounter--;
            playerPiece = previousPiece;
        }
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
        player1CapturedArea.add(capturedPiecesTitle);
        player2CapturedArea.add(capturedPiecesTitle);

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
