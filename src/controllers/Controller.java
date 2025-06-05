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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        view.registerNewGameButton(new MyNewGameListener(model, view, gameTimer));
        view.registerScoreBoardButton(new MyScoreBoardListener(model, view));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(gameTimer.isRunning()) { // kas toimub mäng
            int id = model.checkGridIndex(e.getX(), e.getY());
            int row = model.getRowById(id);
            int col = model.getColById(id);
            // hetke laud
            int[][] matrix = model.getGame().getBoardMatrix();
            model.getGame().setClickCounter(1); // Kliki lugeja
            if(matrix[row][col] == 0) { // 0 on vesi ehk mööda
                model.getGame().setUserClick(row, col, 8);
                //view.getLblShip().setText(String.format("%d / %d", model.getGame().getShipsCounter(), model.getGame().getShipsParts()));
            } else if (matrix[row][col] >= 1 && matrix[row][col] <= 5) { // laevale pihta saamine
                model.getGame().setUserClick(row, col, 7);
                model.getGame().setShipsCounter(1); // Laeva osa leidmine
                view.getLblShip().setText(String.format("%d / %d", model.getGame().getShipsCounter(), model.getGame().getShipsParts()));
            }
            // Näita konsooli mängulauda
            model.getGame().showGameBoard();
            // Uuenda joonistust
            view.repaint();
            // kontrolli mängu lõppu
            checkGameOver();
        }

    }

    private void checkGameOver() {
        if(model.getGame() != null && model.getGame().isGameOver()) {
            gameTimer.stop(); // Peata aeg
            view.getBtnNewGame().setText("Uus Mäng"); // Muuda mängu tekst katkesta mäng => Uus mäng
            // JOptionPane.showMessageDialog(view, "Mängu aeg:" + gameTimer.formatGameTime());
            // Küsi mängija nime
            String name = JOptionPane.showInputDialog(view, "Kuidas on admirali nimi?", "Mäng on läbi", JOptionPane.INFORMATION_MESSAGE);
            if(name == null) { // Vajuta Cancel või suleti aken
                name = "Teadmata";
            }
            if(name.trim().isEmpty()) {
                name = "Teadmata"; // Kasutaja ei sisestanud oma nime, paneme ise nime
            }
            // TODO edetabeli faili ja andmebaasi lisamine (kaks eraldi rida)
            saveEntryToFile(name.trim()); // Edetabeli faili kirjutamine
            // Andmebaasi lisamine
            saveEntryToTable(name.trim());
        }
    }

    private void saveEntryToTable(String name) {
        try(Database db = new Database(model)) {
            db.insert(name, gameTimer.getElapsedSeconds(), model.getGame().getClickCounter(),
                    model.getBoardSize(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEntryToFile(String name) {
        if (model.checkFileExistsAndContent()) {
            // Fail on olemas, kirjutame sisu faili
            File file = new File(model.getScoreFile());
            try(FileWriter fw = new FileWriter(file, true)) {
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                int time = gameTimer.getElapsedSeconds();
                int clicks = model.getGame().getClickCounter();
                int board = model.getBoardSize();
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String datLine = String.join(";", name, String.valueOf(time), String.valueOf(clicks), String.valueOf(board), dateTime);

                pw.println(datLine); // Kirjuta faili
                pw.close(); // sulge fail
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else { // edetabeli faili pole või pole sisu
            // Fail on olemas, kirjutame sisu faili
            File file = new File(model.getScoreFile());
            try(FileWriter fw = new FileWriter(file, true)) {
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(String.join(";", model.getColumnNames()));
                int time = gameTimer.getElapsedSeconds();
                int clicks = model.getGame().getClickCounter();
                int board = model.getBoardSize();
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String datLine = String.join(";", name, String.valueOf(time), String.valueOf(clicks), String.valueOf(board), dateTime);

                pw.println(datLine); // Kirjuta faili
                pw.close(); // sulge fail
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
