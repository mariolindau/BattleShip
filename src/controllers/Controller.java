package controllers;

import controllers.listeners.MyComboBoxListener;
import controllers.listeners.MyScoreBoardListener;
import models.Database;
import models.GameTimer;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller implements MouseListener, MouseMotionListener {
    private Model model;
    private View view;
    private GameTimer gameTimer;
    private Timer guiTimer;
    private boolean isGameRunning = false;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        gameTimer = new GameTimer();

        guiTimer = new Timer(1000, e -> {
            if (gameTimer.isRunning()) {
                this.view.getLblTime().setText(gameTimer.formatGameTime());
            }
        });
        guiTimer.start();

        view.registerComboBox(new MyComboBoxListener(model, view, this));
        view.registerNewGameButton(new MyNewGameListener(model, view, gameTimer, this));
        view.registerScoreBoardButton(new MyScoreBoardListener(model, view, this));
        view.getBtnCloseScoreBoard().addActionListener(e -> view.getPnlScoreBoard().setVisible(false));
    }

    public void startGame() {
        isGameRunning = true;
        view.setControlsEnabled(false);
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameTimer.isRunning()) {
            int id = model.checkGridIndex(e.getX(), e.getY());
            int row = model.getRowById(id);
            int col = model.getColById(id);
            int[][] matrix = model.getGame().getBoardMatrix();
            model.getGame().setClickCounter(1);

            if (matrix[row][col] == 0) {
                model.getGame().setUserClick(row, col, 8);
            } else if (matrix[row][col] >= 1 && matrix[row][col] <= 5) {
                model.getGame().setUserClick(row, col, 7);
                model.getGame().setShipsCounter(1);
                view.getLblShip().setText(String.format("%d / %d", model.getGame().getShipsCounter(), model.getGame().getShipsParts()));
            }

            model.getGame().showGameBoard();
            view.repaint();
            checkGameOver();
        }
    }

    private void checkGameOver() {
        if (model.getGame() != null && model.getGame().isGameOver()) {
            gameTimer.stop();
            view.getBtnNewGame().setText("Uus Mäng");

            String name = JOptionPane.showInputDialog(view, "Kuidas on admirali nimi?", "Mäng on läbi", JOptionPane.INFORMATION_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Teadmata";
            }

            saveEntryToFile(name.trim());
            saveEntryToTable(name.trim());

            isGameRunning = false;
            view.setControlsEnabled(true);

            view.getBtnScoreBoard().setEnabled(true);
            view.getCmbSize().setEnabled(true);

            view.repaint(); // näita lõppseisu

            // Edetabeli avamine eraldi lõimes
            SwingUtilities.invokeLater(() -> {
                new MyScoreBoardListener(model, view, this).actionPerformed(null);
            });
        }
    }

    private void saveEntryToTable(String name) {
        try (Database db = new Database(model)) {
            db.insert(name, gameTimer.getElapsedSeconds(), model.getGame().getClickCounter(),
                    model.getBoardSize(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEntryToFile(String name) {
        File file = new File(model.getScoreFile());
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            if (!model.checkFileExistsAndContent()) {
                pw.println(String.join(";", model.getColumnNames()));
            }

            int time = gameTimer.getElapsedSeconds();
            int clicks = model.getGame().getClickCounter();
            int board = model.getBoardSize();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String datLine = String.join(";", name, String.valueOf(time), String.valueOf(clicks), String.valueOf(board), dateTime);
            pw.println(datLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Hiire muud meetodid
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        String mouse = String.format("x =%03d, y =%03d", e.getX(), e.getY());
        view.getLblMouseXY().setText(mouse);

        int id = model.checkGridIndex(e.getX(), e.getY());
        int row = model.getRowById(id);
        int col = model.getColById(id);

        if (id != -1) {
            view.getLblLID().setText(String.valueOf(id + 1));
        }

        String rowcol = (row == -1 || col == -1) ? "Pole mängulaual" : String.format("%d:%d", row + 1, col + 1);
        view.getLblRowCol().setText(rowcol);
    }

    public void setGameRunning(boolean isRunning) {
        this.isGameRunning = isRunning;
    }
}
