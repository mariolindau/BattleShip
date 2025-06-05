package models;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Model {
    private int boardSize = 10; // Vaikimisi mängulaua suurus
    private ArrayList<GridData> gridData;
    private Game game; // Laevade info mängulaual
    // Edetabeli failiga seotud muutujad
    private String scoreFile = "scores.txt";
    private String[] columnNames = new String[]{"Nimi", "Aeg", "Klikke", "Laua suurus", "Mängu aeg"};
    // Edetabeliga seotud muutujad
    private String scoreDatabase = "scores.db";
    private String scoreTable = "scores";

    public Model() {
        gridData = new ArrayList<>();
    }

    /**
     * Tagastab hiire koordinaatide järgi massiivi indeksi ehk lahtri id
     *
     * @param mouseX on hiire x-koordinaat
     * @param mouseY on hiire y-koordinaat
     * @return lahtri id
     */
    public int checkGridIndex(int mouseX, int mouseY) {
        int result = -1; // Viga
        int index = 0;
        for (GridData gd : gridData) {
            if (mouseX > gd.getX() && mouseX <= gd.getX() + gd.getWidth() && mouseY > gd.getY() && mouseY <= (gd.getY() + gd.getHeight())) {
                result = index;
            }
            index++;
        }
        return result;
    }

    /**
     * Tagastab mängulaua reanumbri saadud id põhjal (checkGridIndex)
     *
     * @param id mängulaua lahtri id
     * @return mängulaua rea number
     */
    public int getRowById(int id) {
        if (id != -1) {
            return gridData.get(id).getRow();
        }
        return -1; // viga
    }

    /**
     * Tagastab mängulaua veeru numbri saadud id põhjal (checkGridIndex
     *
     * @param id mängulaua id
     * @return mängulaua veeru number
     */
    public int getColById(int id) {
        if (id != -1) {
            return gridData.get(id).getCol();
        }
        return -1; // Viga
    }

    public void setupNewGame() {
        game = new Game(boardSize);
    }

    public void drawUserBoard(Graphics g) {
        ArrayList<GridData> gdList = getGridData(); // See loodi mängulaua joonistamisel
        int[][] mattrix = game.getBoardMatrix(); // Siin on laevade vee jms info 0, 1-5, 7, 8

        for (GridData gd : gdList) {
            int row = gd.getRow(); // rida
            int col = gd.getCol(); // veerg
            int cellValue = mattrix[row][col]; // väärtus 0, 1-5, 7 või 8

            // Määrame värvi ja suuruse sõltuvalt lahtri väärtusest (cellValue)
            Color color = null; // algselt värvi pole
            int padding = 0;

            switch (cellValue) { // 0, 1-5, 7, 8
                case 0: //vesi
                    color = new Color(0, 190, 255);
                    break;
                case 7: // Pihtas
                    color = Color.GREEN;
                    break;
                case 8: // Mööda
                    color = Color.RED;
                    padding = 3;
                    break;
                default:
                    if (cellValue >= 1 && cellValue <= 5) {
                        color = new Color(0, 191, 255);
                    }
            }
            // Kui värv on määratud , joonista ruut
            if (color != null) {
                g.setColor(color);
                g.fillRect(
                        gd.getX() + padding,
                        gd.getY() + padding,
                        gd.getWidth() - 2 * padding,
                        gd.getHeight() - 2 * padding
                );
            }
        }

    }

    /**
     * Edetabeli faili olemasolu ja sisu kontroll
     *
     * @return true kui on korras ja false kui pole
     */
    public boolean checkFileExistsAndContent() {
        File file = new File(scoreFile); // Tee scores.txt file objektiks
        if (!file.exists()) { // kui faili ei ole, siis ..
            return false; //.. tagastab false
        }
        try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
            String line = br.readLine();
            if (line == null) {
                return false; // Ridu pole üldse
            }

            String[] columns = line.split(";"); // Semikoolon kindlasti peab olema
            return columns.length == columnNames.length; // Lihtsustatud if lause

        } catch (IOException e) {
            // throw new RuntimeException(e);
            return false;
        }
    }

    /**
     * Edetabeli faili sisu leoatkse massiivi ja tagastatakse
     *
     * @return ScoreData list (edetabeli info)
     */
    public ArrayList<ScoreData> readFromFile() {
        ArrayList<ScoreData> scoreData = new ArrayList<>();
        File file = new File(scoreFile);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
                int lineNumber = 0;
                for (String line; (line = br.readLine()) != null; ) {
                    if (lineNumber > 0) {
                        String[] columns = line.split(";");
                        if (Integer.parseInt(columns[3]) == boardSize) {
                            String name = columns[0];
                            int gameTime = Integer.parseInt(columns[1]);
                            int clicks = Integer.parseInt(columns[2]);
                            int board = Integer.parseInt(columns[3]);
                            LocalDateTime played = LocalDateTime.parse(columns[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            scoreData.add(new ScoreData(name, gameTime, clicks, board, played));
                        }
                    }
                    lineNumber++;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return scoreData; // Tagasta sisu
    }

    // GETTERID

    public int getBoardSize() {
        return boardSize;
    }

    public ArrayList<GridData> getGridData() {
        return gridData;
    }

    public String getScoreFile() {
        return scoreFile;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String getScoreDatabase() {
        return scoreDatabase;
    }

    public String getScoreTable() {
        return scoreTable;
    }

    // SETTERS
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setGridData(ArrayList<GridData> gridData) {
        this.gridData = gridData;
    }

    public Game getGame() {
        return game;
    }

}
