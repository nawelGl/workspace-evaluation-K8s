package fr.upec.episen;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class ProbeController {

    private final DatabaseService dbService;

    public ProbeController(DatabaseService dbService) {
        this.dbService = dbService;
    }

    // Readiness Probe
    @GetMapping("/ready")
    public ResponseEntity<String> ready() {
        if (dbService.isReady()) {
            return ResponseEntity.ok("READY");
        }
        return ResponseEntity.status(503).body("NOT_READY");
    }

    // Liveness Probe
    @GetMapping("/live")
    public ResponseEntity<String> live() {
        if (dbService.isHealthy()) {
            return ResponseEntity.ok("ALIVE");
        }
        return ResponseEntity.status(500).body("DEAD");
    }

    // Endpoint métier pour tester l'app
    @GetMapping("/data")
    public String getData() throws Exception {
        return dbService.executeQuery();
    }
}
