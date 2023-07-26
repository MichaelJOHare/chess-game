package com.michaeljohare.chess.game;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.michaeljohare.chess.GUI.ChessGUI;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Board.initializeBoard();
        ChessGUI chessGUI = new ChessGUI();
    }
}