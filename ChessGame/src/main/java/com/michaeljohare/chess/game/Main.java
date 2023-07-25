package com.michaeljohare.chess.game;

import com.michaeljohare.chess.GUI.ChessGUI;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        Board.initializeBoard();
        ChessGUI chessGUI = new ChessGUI();
    }
}