package com.michaeljohare.chess.GUI;

import com.michaeljohare.chess.game.Board;
import com.michaeljohare.chess.game.Player;
import com.michaeljohare.chess.game.Square;
import com.michaeljohare.chess.pieces.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.michaeljohare.chess.game.Board.*;
import static com.michaeljohare.chess.game.Board.board;

public class ChessGUI extends JFrame {

    /*TO DO:
    * A-H 1-8 Legend
    * Captured pieces font color
    * */

    private JButton[][] chessButtons;
    private JButton playAgainButton;
    private Color defaultButtonColor;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private final Player player1 = new Player(PLAYER_1);
    private final Player player2 = new Player(PLAYER_2);
    private final List<ChessPiece> player1CapturedPieces = new ArrayList<>();
    private final List<ChessPiece> player2CapturedPieces = new ArrayList<>();
    private final List<Square> highlightedSquares = new ArrayList<>();
    private int turnCounter;
    private boolean previousMoveWasCastle = false;
    private boolean isFirstClick = true;
    private boolean pawnPromotionFlag = false;
    private ChessPiece playerPiece;
    private ChessPiece capturedPiece;
    private ChessPiece previousPiece;
    private final String lineBreaks = "\n\n\n\n\n";

    public ChessGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Chess");
        updateFrame(frame);
        frame.setSize(1200, 1000);
        frame.setLayout(new BorderLayout());

        JPanel chessboardPanel = createChessboardPanel();

        logTextArea = createLogTextArea();
        logTextArea.setFont(new Font("Roboto", Font.PLAIN, 20));
        logTextArea.setText("\n\n\n Welcome to Michael's Chess Game! \n Use the undo button to undo a \n previous move. " +
                "\n\n It is White's turn to move first.");
        logScrollPane = new JScrollPane(logTextArea);

        player1CapturedArea = createCapturedArea();
        player2CapturedArea = createCapturedArea();
        updateCapturedPiecesDisplay();

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

        capturedArea.setEditable(false);
        capturedArea.setLayout(new FlowLayout());
        capturedArea.setLineWrap(true);
        capturedArea.setWrapStyleWord(true);

        return capturedArea;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Roboto", Font.BOLD, 24));
        playAgainButton.addActionListener(e -> onPlayAgainButtonClick());
        defaultButtonColor = playAgainButton.getBackground();

        JButton undoButton = new JButton("Undo");
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

        chessButtons[row][col].setBorder(null);

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
                logTextArea.setText(lineBreaks + " There's no piece available in the\n selected square, or the " +
                        "piece you\n selected is not your piece.");
            } else {
                List<Square> moves = playerPiece.getMoves();

                if (moves.size() == 0) {

                    logTextArea.setText(lineBreaks + " The piece you selected does not\n have any legal moves");
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

                previousMoveWasCastle = false;

                if (!isEmpty(row, col)) {
                    playerPiece.movePiece(new Square(row, col));

                    if (turnCounter % 2 == 0) {
                        capturedPiece = player2.getPlayerPiece(new Square(row, col));
                        player2.capturePiece(capturedPiece);
                        player1CapturedPieces.add(capturedPiece);
                        updateCapturedPiecesDisplay();
                    } else {
                        capturedPiece = player1.getPlayerPiece(new Square(row, col));
                        player1.capturePiece(capturedPiece);
                        player2CapturedPieces.add(capturedPiece);
                        updateCapturedPiecesDisplay();
                    }

                    if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0 ||
                            playerPiece.getCurrentSquare().getX() == 7) {
                        pawnPromotionFlag = true;
                    }

                } else {

                    capturedPiece = null;
                    playerPiece.movePiece(new Square(row, col));

                    if (playerPiece instanceof King) {
                        if (turnCounter % 2 == 0 && row == 7 && col == 6) {
                            player1.getPlayerPiece(new Square(7, 7)).movePiece(new Square(7, 5));
                            ((Rook) player1.getPlayerPiece(new Square(7, 5))).hasMoved = true;
                            previousMoveWasCastle = true;
                        }
                        if (turnCounter % 2 == 0 && row == 7 && col == 2) {
                            player1.getPlayerPiece(new Square(7, 0)).movePiece(new Square(7, 3));
                            ((Rook) player1.getPlayerPiece(new Square(7, 3))).hasMoved = true;
                            previousMoveWasCastle = true;
                        }
                        if (turnCounter % 2 == 1 && row == 0 && col == 6) {
                            player2.getPlayerPiece(new Square(0, 7)).movePiece(new Square(0, 5));
                            ((Rook) player2.getPlayerPiece(new Square(0, 5))).hasMoved = true;
                            previousMoveWasCastle = true;
                        }
                        if (turnCounter % 2 == 1 && row == 0 && col == 2) {
                            player2.getPlayerPiece(new Square(0, 0)).movePiece(new Square(0, 3));
                            ((Rook) player2.getPlayerPiece(new Square(0, 3))).hasMoved = true;
                            previousMoveWasCastle = true;
                        }
                        ((King) playerPiece).hasMoved = true;
                    }

                    if (playerPiece instanceof Rook) ((Rook) playerPiece).hasMoved = true;
                    if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0 ||
                            playerPiece.getCurrentSquare().getX() == 7) {
                        pawnPromotionFlag = true;
                    }
                }

                handlePawnPromotion(row);

                updateGUI();
                clearHighlightedSquares();
                previousPiece = playerPiece;
                playerPiece = null;
                isFirstClick = true;
                turnCounter++;

                if (turnCounter % 2 == 0) {
                    logTextArea.setText(lineBreaks + " It's the white player's turn to move.");
                } else if (turnCounter % 2 == 1) {
                    logTextArea.setText(lineBreaks+ " It's the black player's turn to move.");
                }

                handleCheckmate();

            } else {

                logTextArea.setText(lineBreaks + " The square you chose to move to is\n not a legal move, choose a " +
                        "piece\n and try again.");
                clearHighlightedSquares();
                playerPiece = null;
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
                        player2CapturedPieces.remove(capturedPiece);
                    } else {
                        previousPiece.undoMovePiece(capturedPiece.getChessPieceConstant() + player2.getPlayer());
                        player2.undoCapturePiece(capturedPiece);
                        player1CapturedPieces.remove(capturedPiece);
                    }
                } else {
                    if (previousPiece instanceof King) ((King) previousPiece).hasMoved = false;
                    if (previousPiece instanceof Rook) ((Rook) previousPiece).hasMoved = false;

                    if (turnCounter % 2 == 1 && previousMoveWasCastle) {
                        if (board[7][5].equals(ROOK + PLAYER_1)) {
                            ((Rook) player1.getPlayerPiece(new Square(7, 5))).hasMoved = false;
                            player1.getPlayerPiece(new Square(7, 5)).undoMovePiece(EMPTY);
                        } else if (board[7][3].equals(ROOK + PLAYER_1)) {
                            ((Rook) player1.getPlayerPiece(new Square(7, 3))).hasMoved = false;
                            player1.getPlayerPiece(new Square(7, 3)).undoMovePiece(EMPTY);
                        }
                    } else if (turnCounter % 2 == 0 && previousMoveWasCastle){
                        if (board[0][5].equals(ROOK + PLAYER_2)) {
                            ((Rook) player2.getPlayerPiece(new Square(0, 5))).hasMoved = false;
                            player2.getPlayerPiece(new Square(0, 5)).undoMovePiece(EMPTY);
                        } else if (board[0][3].equals(ROOK + PLAYER_2)) {
                            ((Rook) player2.getPlayerPiece(new Square(0, 3))).hasMoved = false;
                            player2.getPlayerPiece(new Square(0, 3)).undoMovePiece(EMPTY);
                        }
                    }
                    previousPiece.undoMovePiece(EMPTY);
                }
            } catch (Exception e) {
                logTextArea.setText(lineBreaks + " You can only undo a previous move \n one time!");
                turnCounter++;
            }
            updateCapturedPiecesDisplay();
            updateGUI();
            turnCounter--;
            playerPiece = null;
            if (turnCounter % 2 == 0) {
                logTextArea.setText(lineBreaks + " It's the white player's turn to move.");
            } else if (turnCounter % 2 == 1) {
                logTextArea.setText(lineBreaks+ " It's the black player's turn to move.");
            }
        }
    }

    private void onPlayAgainButtonClick() {
        Board.initializeBoard();
        player1.resetPlayer(PLAYER_1);
        player2.resetPlayer(PLAYER_2);
        player1CapturedPieces.clear();
        player2CapturedPieces.clear();

        turnCounter = 0;
        previousMoveWasCastle = false;
        isFirstClick = true;
        capturedPiece = null;
        highlightedSquares.clear();
        playerPiece = null;
        previousPiece = null;
        playAgainButton.setBackground(defaultButtonColor);
        playAgainButton.setForeground(null);

        updateCapturedPiecesDisplay();
        updateGUI();
        logTextArea.setText(lineBreaks + " Welcome to Michael's Chess Game! \n Use the undo button to undo a \n previous move. " +
                "\n\n It is White's turn to move first.");
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

        Font capturedPieceFont = new Font("Roboto", Font.PLAIN, 26);
        Font capturedPiecesTitleFont = new Font("Roboto", Font.BOLD, 24);

        Border paddingBorder = BorderFactory.createEmptyBorder(5, 70, 5, 70);
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.gray);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

        JLabel capturedPiecesTitle1 = new JLabel("Captured Pieces");
        capturedPiecesTitle1.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle1.setBorder(compoundBorder);
        JLabel capturedPiecesTitle2 = new JLabel("Captured Pieces");
        capturedPiecesTitle2.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle2.setBorder(compoundBorder);
        player1CapturedArea.add(capturedPiecesTitle1);
        player2CapturedArea.add(capturedPiecesTitle2);

        for (ChessPiece piece : player1CapturedPieces) {
            JLabel blackCapturedPieceLabel = new JLabel(piece.getBlackChessPieceSymbol());
            blackCapturedPieceLabel.setFont(capturedPieceFont);
            player1CapturedArea.add(blackCapturedPieceLabel);
        }

        for (ChessPiece piece : player2CapturedPieces) {
            JLabel whiteCapturedPieceLabel = new JLabel(piece.getWhiteChessPieceSymbol());
            whiteCapturedPieceLabel.setFont(capturedPieceFont);
            player2CapturedArea.add(whiteCapturedPieceLabel);
        }

        player1CapturedArea.revalidate();
        player1CapturedArea.repaint();
        player2CapturedArea.revalidate();
        player2CapturedArea.repaint();
    }

    private void handlePawnPromotion(int row) {

        if (pawnPromotionFlag) {
            if (playerPiece instanceof Pawn && (row == 0 || row == 7)) {
                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                int choice = JOptionPane.showOptionDialog(
                        chessButtons[playerPiece.getCurrentSquare().getX()][playerPiece.getCurrentSquare().getY()],
                        "Select a piece to promote the pawn to:",
                        "Pawn Promotion",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice >= 0 && choice < options.length) {
                    String selectedPiece = options[choice];
                    ((Pawn) playerPiece).promoteTo(selectedPiece);
                }
            }
        }

        pawnPromotionFlag = false;
    }

    private void handleCheckmate() {

        if ((turnCounter % 2 == 0 && player1.getKing().isInCheck())
                || (turnCounter % 2 == 1 && player2.getKing().isInCheck())) {
            logTextArea.setText(lineBreaks + " Check!");
        }

        if ((turnCounter % 2 == 0 && player1.getMoves().size() == 0) ||
                (turnCounter % 2 == 1 && player2.getMoves().size() == 0)) {

            if (turnCounter % 2 == 0) {
                logTextArea.setText(lineBreaks + "     Player 2 has won the game!\n\n\tPlay again?");
            } else {
                logTextArea.setText(lineBreaks + "     Player 1 has won the game!\n\n\tPlay again?");
            }

            playAgainButton.setBackground(Color.GREEN);
            playAgainButton.setForeground(Color.BLACK);
        }
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
