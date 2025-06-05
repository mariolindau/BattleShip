package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Database implements AutoCloseable {
    private Connection connection;
    private String dbFile;
    private String dbUrl;
    private String tableName;

    public Database(Model model) throws SQLException {
        this.dbFile = model.getScoreDatabase();
        this.dbUrl = "jdbc:sqlite:" + dbFile; // Andmebaasiga ühendamiseks
        this.tableName = model.getScoreTable();


        // Alustame kohe ühendusega
        connect();
        ensureTableExists(); // Veendu kas andmebaasis on tabel olemas ja vajaduse tee
    }

    private void ensureTableExists() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "time INTEGER NOT NULL," +
                "clicks INTEGER," +
                "board_size INTEGER," +
                "game_time TEXT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSql); // Loob tabeli kui pole
            System.out.println("Tabel kontrollitud/loodud: " + tableName); // Test
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println("Viga tabeli loomisel: " + e.getMessage());
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
        System.out.println("Ühendus loodud" + dbUrl); // Test
    }


    @Override
    public void close() throws Exception {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("{Ühendus suletud"); // test
            } catch (SQLException e) {
                System.err.println("Viga ühenduse loomisel: " + e.getMessage());
            }
        }

    }

    /**
     * Mängija andmete lisamine andmebaasi tabelisse
     *
     * @param name      Nimi
     * @param time      Mänguaeg sekundites
     * @param clicks    Klikkimiste arv
     * @param boardSize Mängu laua suurus
     * @param played    Mängu kuupäev ja kellaaeg
     */
    public void insert(String name, int time, int clicks, int boardSize, String played) {
        String sql = "INSERT INTO " + tableName + " (name, time, clicks, board_size, game_time) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, time);
            stmt.setInt(3, clicks);
            stmt.setInt(4, boardSize);
            stmt.setString(5, played);
            stmt.executeUpdate(); // Käivitab päriselt lisamise (INSERT INTO ...)
        } catch (SQLException e) {
            // throw new RuntimeException(e);
            System.err.println("Viga andmete lisamisel: " + e.getMessage());
        }
    }

    public ArrayList<ScoreData> select(int boardSize) {
        ArrayList<ScoreData> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE board_size = ? ORDER BY time, clicks, game_time;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int time = rs.getInt("time");
                    int clicks = rs.getInt("clicks");
                    int size = rs.getInt("board_size");
                    String gameTime = rs.getString("game_time");

                    // Teisendame GameTime LocalDateTime
                    LocalDateTime played = LocalDateTime.parse(gameTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    ScoreData data = new ScoreData(name, time, clicks, size, played);
                    results.add(data);
                }
            }
        } catch (SQLException e) {
            System.err.println("Viga SELect päringus: " + e.getMessage());
            System.out.println(sql); //????
        }
        return results;
    }
}