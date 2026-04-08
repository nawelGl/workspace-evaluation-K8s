package fr.upec.episen;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseService {
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private boolean isBlocked = false;
    private boolean isReady = false;

    public DatabaseService() {
        new Thread(() -> {
            try {
                Thread.sleep(20000);
                this.isReady = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public String executeQuery() throws Exception {
        if (isBlocked) {
            throw new RuntimeException("DATABASE_CRITICAL_ERROR: Connection pool exhausted");
        }

        int currentCount = requestCounter.incrementAndGet();

        // Blocage définitif
        if (currentCount >= 30) {
            isBlocked = true;
            throw new RuntimeException("FATAL: Database connection blocked forever");
        }

        // Lenteur toutes les 10 utilisations
        if (currentCount % 10 == 0) {
            Thread.sleep(60000);
        }

        return "Données récupérées (Requête #" + currentCount + ")";
    }

    public boolean isReady() {
        return isReady && !isBlocked;
    }

    public boolean isHealthy() {
        return !isBlocked;
    }
}