package fr.upec.episen;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StressController {

    @GetMapping("/cpu")
    public ResponseEntity<String> pullCpu(@RequestParam(defaultValue = "10") int duration) {
        long end = System.currentTimeMillis() + (duration * 1000L);
        
        while (System.currentTimeMillis() < end) {
            Math.sqrt(Math.random()); // Simulation de charge
        }
        
        return ResponseEntity.ok("Calcul terminé sur " + System.getenv("HOSTNAME"));
    }
}