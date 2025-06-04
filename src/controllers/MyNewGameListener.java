package controllers;

import models.GameTimer;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyNewGameListener implements ActionListener {
    private Model model;
    private View view;
    private GameTimer gameTimer;

    public MyNewGameListener(Model model, View view, GameTimer gameTimer) {
        this.model = model;
        this.view = view;
        this.gameTimer = gameTimer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Ei kliki mind :)");
        if (!gameTimer.isRunning()) { // Mäng ei käi
            // See on uus lahendus
            new Thread(() -> {
                model.setupNewGame(); // Teeme uue mängu
                model.getGame().setupGameBoard(); // Seadistame mängulaua
                model.getGame().showGameBoard(); // Näita konsooli mängulaua sisu
                view.getLblShip().setText(model.getGame().getShipsCounter() + " / " + model.getGame().getShipsParts());
                // Gui uuendused Swing lõimes (mahukas ja kasutajaliides ei hangu)
                SwingUtilities.invokeLater(() -> {
                    view.getBtnNewGame().setText("Katkesta mäng"); // Muuda uus mäng => katkesta mäng
                    gameTimer.start(); // Käivita aeg
                });
            }).start();
        } else { // Meil on mäng pooleli
            gameTimer.stop(); // Peata aeg
            view.getBtnNewGame().setText("Uus Mäng"); // Muuda katkesta mäng => Uus mäng
        }
    }
}
