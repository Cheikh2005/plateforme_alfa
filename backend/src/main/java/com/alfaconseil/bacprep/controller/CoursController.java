package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.model.Cours;
import com.alfaconseil.bacprep.model.Matiere;
import com.alfaconseil.bacprep.model.UserProgress;
import com.alfaconseil.bacprep.service.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CoursController {

    @Autowired
    private CoursService coursService;

    @GetMapping("/matieres")
    public ResponseEntity<List<Matiere>> getMatieres() {
        return ResponseEntity.ok(coursService.getAllMatieres());
    }

    @GetMapping("/cours")
    public ResponseEntity<List<Cours>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }

    @GetMapping("/cours/matiere/{matiereId}")
    public ResponseEntity<List<Cours>> getCoursByMatiere(@PathVariable Long matiereId) {
        return ResponseEntity.ok(coursService.getCoursByMatiere(matiereId));
    }

    @GetMapping("/cours/{id}")
    public ResponseEntity<?> getCoursById(@PathVariable Long id) {
        return coursService.getCoursById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cours/{id}/progress")
    public ResponseEntity<?> markProgress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer pourcentage = body.getOrDefault("pourcentage", 100);
            UserProgress progress = coursService.markProgress(userDetails.getUsername(), id, pourcentage);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<List<UserProgress>> getProgress(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(coursService.getUserProgress(userDetails.getUsername()));
    }
}