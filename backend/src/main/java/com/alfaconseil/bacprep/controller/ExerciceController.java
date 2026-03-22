package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.dto.SubmitAnswerRequest;
import com.alfaconseil.bacprep.model.Exercice;
import com.alfaconseil.bacprep.model.UserScore;
import com.alfaconseil.bacprep.service.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercices")
public class ExerciceController {

    @Autowired
    private ExerciceService exerciceService;

    @GetMapping
    public ResponseEntity<List<Exercice>> getAllExercices() {
        return ResponseEntity.ok(exerciceService.getAllExercices());
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<Exercice>> getByMatiere(@PathVariable Long matiereId) {
        return ResponseEntity.ok(exerciceService.getExercicesByMatiere(matiereId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Exercice>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(exerciceService.getExercicesByType(type));
    }

    @GetMapping("/matiere/{matiereId}/type/{type}")
    public ResponseEntity<List<Exercice>> getByMatiereAndType(
            @PathVariable Long matiereId, @PathVariable String type) {
        return ResponseEntity.ok(exerciceService.getExercicesByMatiereAndType(matiereId, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return exerciceService.getExerciceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SubmitAnswerRequest request) {
        try {
            Map<String, Object> result = exerciceService.submitAnswers(userDetails.getUsername(), request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/scores")
    public ResponseEntity<List<UserScore>> getScores(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(exerciceService.getUserScores(userDetails.getUsername()));
    }
}