package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.dto.CreateUserRequest;
import com.alfaconseil.bacprep.model.Role;
import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest req) {
        try {
            if (userRepository.existsByUsername(req.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nom d'utilisateur déjà utilisé"));
            }
            if (userRepository.existsByEmail(req.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email déjà utilisé"));
            }
            User user = new User();
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setNom(req.getNom());
            user.setPrenom(req.getPrenom());
            user.setSerie(req.getSerie() != null ? req.getSerie() : "C");
            user.setRole(req.getRole() != null ? Role.valueOf(req.getRole()) : Role.ETUDIANT);
            user.setActif(true);
            user.setDateInscription(LocalDateTime.now());
            User saved = userRepository.save(user);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            if (updates.containsKey("nom")) user.setNom((String) updates.get("nom"));
            if (updates.containsKey("prenom")) user.setPrenom((String) updates.get("prenom"));
            if (updates.containsKey("serie")) user.setSerie((String) updates.get("serie"));
            if (updates.containsKey("role")) user.setRole(Role.valueOf((String) updates.get("role")));
            if (updates.containsKey("actif")) user.setActif((Boolean) updates.get("actif"));
            if (updates.containsKey("password") && updates.get("password") != null && !((String) updates.get("password")).isEmpty()) {
                user.setPassword(passwordEncoder.encode((String) updates.get("password")));
            }
            User saved = userRepository.save(user);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}