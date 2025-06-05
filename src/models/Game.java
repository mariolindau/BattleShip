package models;

import java.util.Random;
import java.util.stream.IntStream;

public class Game {
    // TODO laevade paigutamisel pannakse igale laevale kaitse tsoon aga tegelikult pole vajalik
    private int boardSize;          // Mängulaua suuruse vaikimisi on 10X10
    private int[][] boardMatrix;  // Mängulaual asuvad laevad
    private Random random = new Random(); // Juhuslikkuse jaoks
    //private int[] ships = {4, 3, 3, 2, 2, 2, 1}; // Laeva pikkused US versioon
    //private int[] ships = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1}; // Õpilased
    private int[] ships = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Levade pikkused EE
    private int shipsCounter = 0; // Laevu kokku
    private int clickCounter = 0; // Mitu korda klikiti mängus

    public Game(int boardSize) {
        this.boardSize = boardSize;
        this.boardMatrix = new int[boardSize][boardSize]; // Uue mängulaua loomine
    }

    /**
     * Näita konsoolis mängulaua sisu
     */
    public void showGameBoard() {
        System.out.println(); // Tühi rida
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                System.out.print(boardMatrix[row][col] + " ");
            }
            System.out.println(); // Veeru lõpus uuele reale
        }
    }

    public void setupGameBoard() {
        boardMatrix = new int[boardSize][boardSize]; // Uus Laua suurus (algseis on 0)
        int shipsTotal = ships.length; // Kui palju on laevu kokku
        int shipsPlaced = 0; // KUi palju laevu on paigutatud
        // TODO Laevade järjekorra segamine

        while (shipsPlaced < shipsTotal) {
            int length = ships[shipsPlaced]; // Millist laeva paigutada (laeva pikkus)
            boolean placed = false; // laeva ei ole paigutatud

            // Valime juhusliku alguspunkti
            int startRow = random.nextInt(boardSize); // Rida
            int startCol = random.nextInt(boardSize); // Veerg

            // Käime kogu laua läbi alates sellest punktist
            outerloop:
            // Lihtsalt silt (Tabel) ehk nimi for-loopile
            for (int roffset = 0; roffset < boardSize; roffset++) { // Rida
                int r = (startRow + roffset) % boardSize;
                for (int colffset = 0; colffset < boardSize; colffset++) { // Veerg
                    int c = (startCol + colffset) % boardSize;

                    boolean vertical = random.nextBoolean(); // Määrame juhusliku suuna true = vertical
                    if (tryPlaceShip(r, c, length, vertical) || tryPlaceShip(r, c, length, !vertical)) {
                        placed = true; // lAEV PAIGUTATUD
                        break outerloop; // Katkesta mõlemad for-loop kordused
                    }
                }
            }
            if (placed) {
                shipsPlaced++; // Järgmine laev
            } else {
                // Kui ei leitud sobivat kohta, katkestame ja alustame uuesti
                setupGameBoard();
                return;
            }
        }
        // Eemaldame ajutised kaitsetsoonid (0-9), jättes alles ainlt laevad (1-4) ja tühjad veekohad
        replaceNineToZero();
    }

    private void replaceNineToZero() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardMatrix[row][col] == 9) {
                    boardMatrix[row][col] = 0;

                }
            }
        }
    }

    private boolean tryPlaceShip(int row, int col, int length, boolean vertical) {
        // Kontrolli kas laev üldse mahub mängulauale
        if (vertical && row + length > boardSize) return false;
        if (!vertical && col + length > boardSize) return false;

        // Kontrolli kas sihtpiirkond on vaba (s.h. kaitsetsoon)
        if (!canPlaceShip(row, col, length, vertical)) return false;

        // Kirjutame laeva mängu lauale: paigutame igasse lahtrisse laeva pikkuse
        for (int i = 0; i < length; i++) {
            int r = vertical ? row + i : row; // Kustutame rida või mitte, olenevalt suunast
            int c = vertical ? col : col + i; // Sama veeru kohta
            boardMatrix[r][c] = length; // Määrame laeva lahtrisse selle pikkuse
        }
        // Määrame ümber laeva kaitsetsooni ( vältimaks kontaktset paigutust
        makeSurrounding(row, col, length, vertical);
        return true;
    }

    private void makeSurrounding(int row, int col, int length, boolean vertical) {
        Area area = getShipsSurroundingArea(row, col, length, vertical);
        //Käime ala igas lahtris ja kui seal on vesi (0), siis märgime selle kaitseks (9)
        for (int r = area.startRow; r <= area.endRow; r++) {
            for (int c = area.startCol; c <= area.endCol; c++) {
                if (boardMatrix[r][c] == 0) { // kas on vest
                    boardMatrix[r][c] = 9; // Pane kaitse
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int length, boolean vertical) {
        Area area = getShipsSurroundingArea(row, col, length, vertical);
        // Kontrollime igat lahtrit alal - kui kuskil pole tühjust (0), katkestame
        for (int r = area.startRow; r <= area.endRow; r++) {
            for (int c = area.startCol; c <= area.endCol; c++) {
                if (boardMatrix[r][c] > 0 && boardMatrix[r][c] <= 5) return false; // 05.06.25 tehtud parandus
            }
        }
        return true; // Kõik kohad olid vabad
    }

    private Area getShipsSurroundingArea(int row, int col, int length, boolean vertical) {
        // arvutame ümbritseva ala piirid, hoides neid mängulaua piires
        int startRow = Math.max(0, row - 1);
        int endRow = Math.min(boardSize - 1, vertical ? row + length : row + 1);
        int startCol = Math.max(0, col - 1);
        int endCol = Math.min(boardSize - 1, vertical ? col + 1 : col + length);
        return new Area(startRow, endRow, startCol, endCol);
    }

    /**
     * Selles lahtris klikkis kasutaja hiirega, kas sai pihta või läks mööda
     *
     * @param row  rida
     * @param col  veerg
     * @param what millega tegu (7 pihtas, 8 möödas)
     */
    public void setUserClick(int row, int col, int what) {
        if (what == 7) {
            boardMatrix[row][col] = 7; // Pihtas
        } else if (what == 8) {
            boardMatrix[row][col] = 8; // Möödas
        }
    }


// GETTERS

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    public int getShipsCounter() {
        return shipsCounter;
    }

    public int getClickCounter() {
        return clickCounter;
    }

    /**
     * {4, 3, 3 jne} Laevade summa näide on 10 (4, 3, 3)
     *
     * @return Laevade pikkuste summa
     */
    public int getShipsParts() {
        return IntStream.of(ships).sum();
    }

    /**
     * Kas mäng on läbi
     * @return true kui on läbi ja false kui pole läbi
     */
    public boolean isGameOver() {
        return getShipsParts() == getShipsCounter();
    }

// Setters

    /**
     * Suurendab leitud laevade kogust etteantud väärtuse võrra
     *
     * @param shipsCounter etteantud väärtus (1)
     */
    public void setShipsCounter(int shipsCounter) {
        this.shipsCounter += shipsCounter;
    }

    public void setClickCounter(int clickCounter) {
        this.clickCounter += clickCounter;
    }
}
