package com.michaeljohare.chess.game;

import com.michaeljohare.chess.pieces.*;

import java.util.List;
import java.util.Scanner;

import static com.michaeljohare.chess.game.Board.*;

public class Play {

    private int turnCounter;
    private boolean hasCastled = false;
    private Player player1 = new Player(PLAYER_1);
    private Player player2 = new Player(PLAYER_2);
    private ChessPiece capturedPiece;
    private ChessPiece playerPiece;
    private ChessPiece previousPiece;
    private Scanner userInput = new Scanner(System.in);

    public int getTurnCounter() {
        return turnCounter;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private void pawnPromote(int x, int y, Player player) {
        switch (userInput.nextInt()) {
            case 1: player.promotePawn(new Rook(new Square(x, y), player1)); break;
            case 2: player.promotePawn(new Knight(new Square(x, y), player1)); break;
            case 3: player.promotePawn(new Bishop(new Square(x, y), player1)); break;
            case 4: player.promotePawn(new Queen(new Square(x, y), player1)); break;
        }
    }

    public void play() {
        displayBoard();
        int x = -1;
        int y = -1;

        if (turnCounter % 2 == 0) {
            System.out.println("It's Player 1's turn to make a move\n");
        } else {
            System.out.println("It's Player 2's turn to make a move\n");
        }

        if (turnCounter > 0) System.out.println("Enter \"Undo\" to undo previous move");

        System.out.print("Enter the corresponding" +
                " letter/number combination to select a piece (eg. G1 to select the pawn on G1) >>> ");
        String playerInput = userInput.nextLine();
        if (playerInput.equalsIgnoreCase("Undo")) {
            try {
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
            play();
        }

        if (!playerInput.equalsIgnoreCase("undo")) {
            try {
                x = (8 - Integer.parseInt(playerInput.substring(1)));
                for (int i = 0; i < legendLetter.length; i++) {
                    if (playerInput.substring(0, 1).equalsIgnoreCase(legendLetter[i])) {
                        y = i;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("\nInvalid selection, try again.");
                play();
            }
        }

        if (!playerInput.equalsIgnoreCase("undo")) {
            try {
                if (isEmpty(x, y)) {
                    System.out.println("\nThere is not a selectable piece in the square you chose");
                    playerPiece = previousPiece;
                    play();
                }
            } catch (Exception e) {
                System.out.println("\nInvalid selection, try again.");
                playerPiece = previousPiece;
                play();
            }
        }

        if (!playerInput.equalsIgnoreCase("undo")) {
            if (turnCounter % 2 == 0) {
                playerPiece = player1.getPlayerPiece(new Square(x, y));
            } else {
                playerPiece = player2.getPlayerPiece(new Square(x, y));
            }

            if (playerPiece == null) {
                System.out.println("\nYou must choose your own piece\n");
                playerPiece = previousPiece;
                play();
            } else {
                List<Square> moves = playerPiece.getMoves();
                if (moves.size() == 0) {
                    System.out.println("\nThe piece you selected does not have any legal moves");
                    playerPiece = previousPiece;
                    play();
                }
                System.out.println("\nThese are the available moves");
                for (Square square : moves) {
                    System.out.println(square);
                }
                System.out.print("\nEnter the corresponding letter/number combination of the square you " +
                        "would like to move to >>> ");
                playerInput = userInput.nextLine();
                x = -1;
                y = -1;
                try {
                    x = (8 - Integer.parseInt(playerInput.substring(1)));
                    for (int i = 0; i < legendLetter.length; i++) {
                        if (playerInput.substring(0, 1).equalsIgnoreCase(legendLetter[i])) {
                            y = i;
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Square selection was not a valid input, try again.");
                    playerPiece = previousPiece;
                    play();
                }

                if (moves.contains(new Square(x, y))) {
                    if (!isEmpty(x, y)) {
                        playerPiece.movePiece(new Square(x, y));
                        if (turnCounter % 2 == 0) {
                            capturedPiece = player2.getPlayerPiece(new Square(x, y));
                            player2.capturePiece(capturedPiece);
                            hasCastled = false;
                            if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0) {
                                System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                        "to promote to rook" +
                                        "\n\n1)Rook" +
                                        "\n2)Knight" +
                                        "\n3)Bishop" +
                                        "\n4)Queen" +
                                        "\n\nEnter your choice >>> ");
                                pawnPromote(x, y, player1);
                            }
                        } else {
                            capturedPiece = player1.getPlayerPiece(new Square(x, y));
                            player1.capturePiece(capturedPiece);
                            hasCastled = false;
                            if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 7) {
                                System.out.print("What do you want to promote to? Enter corresponding number eg. 1 " +
                                        "to promote to rook" +
                                        "\n\n1)Rook" +
                                        "\n2)Knight" +
                                        "\n3)Bishop" +
                                        "\n4)Queen" +
                                        "\n\nEnter your choice >>> ");
                                pawnPromote(x, y, player2);
                            }
                        }
                    } else {
                        capturedPiece = null;
                        playerPiece.movePiece(new Square(x, y));
                        hasCastled = false;
                        if (playerPiece instanceof King) {
                            if (turnCounter % 2 == 0 && x == 7 && y == 6) {
                                player1.getPlayerPiece(new Square(7, 7)).movePiece(new Square(7, 5));
                                ((Rook) player1.getPlayerPiece(new Square(7, 5))).hasMoved = true;
                                hasCastled = true;
                            }
                            if (turnCounter % 2 == 0 && x == 7 && y == 2) {
                                player1.getPlayerPiece(new Square(7, 0)).movePiece(new Square(7, 3));
                                ((Rook) player1.getPlayerPiece(new Square(7, 3))).hasMoved = true;
                                hasCastled = true;
                            }
                            if (turnCounter % 2 == 1 && x == 0 && y == 6) {
                                player2.getPlayerPiece(new Square(0, 7)).movePiece(new Square(0, 5));
                                ((Rook) player2.getPlayerPiece(new Square(0, 5))).hasMoved = true;
                                hasCastled = true;
                            }
                            if (turnCounter % 2 == 1 && x == 0 && y == 2) {
                                player2.getPlayerPiece(new Square(0, 0)).movePiece(new Square(0, 3));
                                ((Rook) player2.getPlayerPiece(new Square(0, 3))).hasMoved = true;
                                hasCastled = true;
                            }
                            ((King) playerPiece).hasMoved = true;
                        }
                        if (playerPiece instanceof Rook) ((Rook) playerPiece).hasMoved = true;
                        if (playerPiece instanceof Pawn && playerPiece.getCurrentSquare().getX() == 0) {
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
                        }
                    }
                    previousPiece = playerPiece;
                    turnCounter++;
                } else {
                    System.out.println("Invalid square selection.");
                    playerPiece = previousPiece;
                }
            }
        }
        if ((turnCounter % 2 == 0 && player1.getKing().isInCheck())
                || (turnCounter % 2 == 1 && player2.getKing().isInCheck())) {
            System.out.println("\n\nCheck!");
        }
    }
    private boolean isEmpty(int x, int y) {
        return board[x][y].equals(EMPTY);
    }
}
