package models;

import java.time.Duration;
import java.time.Instant;

/**
 * Lihtne mängu ajamõõtja, mis võimaldab:
 *  - aja käivitamist
 *  - aja peatamist
 *  - nullist uuesti alustamist
 *  Aega mõõdetakse süsteemi kella alusel
 */
public class GameTimer {
    private Instant startTime;
    private boolean running;
    private Duration duration = Duration.ZERO;

    /**
     * Käivitab taimeri nullist. Kui juba töötas, siis alustab uuesti algusest
     */
    public void start() {
        startTime = Instant.now(); // Alustame aja mõõtmist praegusest ajahetkest
        running = true;
        duration = duration.ZERO; // Taaskäivitus nullist
    }

    /**
     * Peatab Timeri, aeg peatub
     */
    public void stop() {
        if (running && startTime != null) {
            duration = duration.between(startTime, Instant.now());
        }
        running = false;
    }

    /**
     * Kas Timer töötab
     * @return true jah, false ei
     */
    public boolean isRunning() {
        return running;
    }
    public int getElapsedSeconds() {
        if(startTime == null) {
            return 0;
        }
        if (running) {
            return (int) Duration.between(startTime, Instant.now()).getSeconds();
        } else {
            return (int) duration.getSeconds();
        }
    }

    /**
     * Vormindab aja sekunditest kujule MM:SS ehk 25 sek = 00:25
     * @return võrmindatud aeg
     */
    public String formatGameTime() {
        int totalSeconds = getElapsedSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
