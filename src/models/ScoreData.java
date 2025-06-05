package models;

import java.time.LocalDateTime;

public class ScoreData implements Comparable<ScoreData> {
    private String name;
    private int time; // Mängu aeg sekundites 68 (01:08)
    private int clicks;
    private int board; // Mängu laua suurus 10
    private LocalDateTime playedTime; // Mängu aeg kuupäev, kellaaeg

    // Getters
    public ScoreData(String name, int time, int clicks, int board, LocalDateTime playedTime) {
        this.name = name;
        this.time = time;
        this.clicks = clicks;
        this.board = board;
        this.playedTime = playedTime;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    // Setters
    public int getClicks() {
        return clicks;
    }

    public int getBoard() {
        return board;
    }

    public LocalDateTime getPlayedTime() {
        return playedTime;
    }

    @Override
    public String toString() {
        return "ScoreData{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", clicks=" + clicks +
                ", board=" + board +
                ", playedTime=" + playedTime +
                '}';
    }

    @Override
    public int compareTo(ScoreData o) {
        // Kasutamine mujal: Collections.sort(MASSIIVINIMI)
        int cmp = Integer.compare(this.time, o.time);
        if (cmp != 0) return cmp;

        cmp = Integer.compare(this.clicks, o.clicks);
        if (cmp != 0) return cmp;

        return this.playedTime.compareTo(o.playedTime);
    }

    /**
     * Vormindab etteantud aja sekundid kujule MM:SS
     * @param seconds sekundid kujul 98
     * @return vormindatud aeg
     */
    public String formatGameTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}



