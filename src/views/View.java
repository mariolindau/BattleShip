package views;

import models.Model;
import views.panels.GameBoard;
import views.panels.InfoBoard;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private Model model;
    private GameBoard gameBoard; // Mängulaud
    private InfoBoard infoBoard; // Teadetetahvel

    public View(Model model) {
        super("laevade pommitamine"); //Akna nimi
        this.model = model;

        gameBoard = new GameBoard(model); // Mängulaua loomine
        infoBoard = new InfoBoard();

        JPanel container = new JPanel(new BorderLayout());

        container.add(gameBoard, BorderLayout.CENTER);  // Mängulaud ujuvale osale
        container.add(infoBoard, BorderLayout.EAST);    // Teadete tahvel vasakule serva

        add(container);
    }
}
