package controllers;

import controllers.listeners.MyComboBoxListener;
import models.GameTimer;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Controller implements MouseListener, MouseMotionListener {
    private Model model;
    private View view;
    private GameTimer gameTimer;
    private Timer guiTimer;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        gameTimer = new GameTimer(); // Loome aja objekti aga ei käivita

        guiTimer = new Timer(1000, e ->{
            if (gameTimer.isRunning()) {
                this.view.getLblTime().setText(gameTimer.formatGameTime());
            }
        });
        guiTimer.start(); // Käivitab GUI taimeri ahga mängu aega (GameTimer) mitte;

        //Listenerid
        view.registerComboBox(new MyComboBoxListener(model, view));
        view.registerNewGameButton(new MyNewGameListener(model, view, gameTimer)); // TODO GameTimer
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // System.out.println("Mouse Moved"); Testimiseks
        String mouse = String.format("x =%03d, y =%03d", e.getX(), e.getY());
        view.getLblMouseXY().setText(mouse);

        // TODO loe id, row ja col infot
        int id = model.checkGridIndex(e.getX(), e.getY());
        int row = model.getRowById(id);
        int col = model.getColById(id);

        // Id näitamine labelil
        if (id != -1) {
            view.getLblLID().setText(String.valueOf(id + 1)); // inimlik
        }
        // Row and Col näitamine
        String rowcol = String.format("%d:%d", row + 1, col + 1);
        if(row == -1 || col == -1) {
            rowcol = "Pole mängulaual";
        }
        view.getLblRowCol().setText(rowcol);
    }
}
