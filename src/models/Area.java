package models;

public class Area {
    int startRow; // Ala ülemise rea indeks
    int endRow; // Ala alusel rea indeks
    int startCol; // Ala vasakpoole veeru indeks
    int endCol; // Ala parempoolse veeru indeks

    public Area(int startRow, int endRow, int startCol, int endCol) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }
}
