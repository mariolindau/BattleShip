package controllers.listeners;

import controllers.Controller;
import models.Database;
import models.Model;
import models.ScoreData;
import views.View;
import views.dialogs.ScoreBoardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class MyScoreBoardListener implements ActionListener {
    private Model model;
    private View view;
    private Controller controller;
    private JDialog dlgScoreBoard;

    public MyScoreBoardListener(Model model, View view, Controller controller) {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller.isGameRunning()) {
            JOptionPane.showMessageDialog(view,
                    "Mäng käib! Edetabelit saab vaadata pärast mängu",
                    "Hoiatus", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<ScoreData> result;
        if (view.getRdoFile().isSelected()) {
            result = model.readFromFile();
            if (createTable(result)) {
                if (view.getChkSeparateWindow().isSelected()) {
                    setupDlgScoreBoard();
                } else {
                    view.initScoreBoardPanel(view.getPnlScoreBoard(), view.getTblScoreBoard(), view.getBtnCloseScoreBoard());
                }
            } else {
                JOptionPane.showMessageDialog(view, "Andmeid pole");
            }
        } else {
            try (Database db = new Database(model)) {
                result = db.select(model.getBoardSize());
                if (!result.isEmpty() && createTableDb(result)) {
                    if (view.getChkSeparateWindow().isSelected()) {
                        setupDlgScoreBoard();
                    } else {
                        view.initScoreBoardPanel(view.getPnlScoreBoard(), view.getTblScoreBoard(), view.getBtnCloseScoreBoard());
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Andmebaasi tabel on tühi");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Viga andmebaasi töötlemisel: " + ex.getMessage(),
                        "Viga", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private boolean createTableDb(ArrayList<ScoreData> result) {
        if (!result.isEmpty()) {
            String[][] data = new String[result.size()][5];
            for (int i = 0; i < result.size(); i++) {
                data[i][0] = result.get(i).getName();
                data[i][1] = result.get(i).formatGameTime(result.get(i).getTime());
                data[i][2] = String.valueOf(result.get(i).getClicks());
                data[i][3] = String.valueOf(result.get(i).getBoard());
                data[i][4] = result.get(i).getPlayedTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, model.getColumnNames()) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = view.getTblScoreBoard();
            table.setModel(tableModel);

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();
                        int row = table.rowAtPoint(e.getPoint());
                        int col = table.columnAtPoint(e.getPoint());

                        StringBuilder rowData = new StringBuilder();
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            rowData.append(table.getValueAt(row, i)).append(" | ");
                        }
                        JOptionPane.showMessageDialog(table, "Valitud rida:\n" + rowData);

                        Object cellObject = table.getValueAt(row, col);
                        JOptionPane.showMessageDialog(view, "Valitud lahter:\n " + cellObject);
                    }
                }
            });

            JTableHeader header = table.getTableHeader();
            header.setFont(header.getFont().deriveFont(Font.BOLD));

            int[] columnWidths = {100, 120, 80, 90, 150};
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            }

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            for (int i = 1; i < model.getColumnNames().length; i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }

            dlgScoreBoard = new ScoreBoardDialog(view);
            dlgScoreBoard.add(new JScrollPane(table));
            dlgScoreBoard.setTitle("Edetabeli andmebaas");

            return true;
        }
        return false;
    }

    private boolean createTable(ArrayList<ScoreData> result) {
        if (!result.isEmpty()) {
            Collections.sort(result);
            String[][] data = new String[result.size()][5];
            for (int i = 0; i < result.size(); i++) {
                data[i][0] = result.get(i).getName();
                data[i][1] = result.get(i).formatGameTime(result.get(i).getTime());
                data[i][2] = String.valueOf(result.get(i).getClicks());
                data[i][3] = String.valueOf(result.get(i).getBoard());
                data[i][4] = result.get(i).getPlayedTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            }

            JTable table = view.getTblScoreBoard();
            table.setModel(new DefaultTableModel(data, model.getColumnNames()));

            int[] columnWidths = {100, 80, 60, 80, 160};
            for (int i = 0; i < columnWidths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            }

            dlgScoreBoard = new ScoreBoardDialog(view);
            dlgScoreBoard.add(new JScrollPane(table));
            dlgScoreBoard.setTitle("Edetabel Failist");

            return true;
        }
        return false;
    }

    private void setupDlgScoreBoard() {
        dlgScoreBoard.setModal(true);
        dlgScoreBoard.pack();
        dlgScoreBoard.setLocationRelativeTo(null);
        dlgScoreBoard.setVisible(true);
    }
}
