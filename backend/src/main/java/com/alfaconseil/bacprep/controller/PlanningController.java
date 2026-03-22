package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.model.Planning;
import com.alfaconseil.bacprep.service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plannings")
public class PlanningController {

    @Autowired
    private PlanningService planningService;

    @GetMapping
    public ResponseEntity<List<Planning>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(planningService.getUserPlannings(userDetails.getUsername()));
    }

    @GetMapping("/week")
    public ResponseEntity<List<Planning>> getByWeek(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(planningService.getPlanningsByWeek(userDetails.getUsername(), debut, fin));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Planning planning) {
        try {
            Planning created = planningService.createPlanning(userDetails.getUsername(), planning);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody Planning updates) {
        try {
            Planning updated = planningService.updatePlanning(id, userDetails.getUsername(), updates);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            Planning updated = planningService.toggleComplete(id, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            planningService.deletePlanning(id, userDetails.getUsername());
            return ResponseEntity.ok(Map.of("message", "Tâche supprimée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}