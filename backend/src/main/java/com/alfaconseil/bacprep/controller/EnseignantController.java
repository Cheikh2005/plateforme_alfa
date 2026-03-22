package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.dto.CreateCoursRequest;
import com.alfaconseil.bacprep.dto.CreateExerciceRequest;
import com.alfaconseil.bacprep.dto.CreateQuestionRequest;
import com.alfaconseil.bacprep.model.*;
import com.alfaconseil.bacprep.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enseignant")
@PreAuthorize("hasRole('ENSEIGNANT') or hasRole('ADMIN')")
public class EnseignantController {

    @Autowired private CoursRepository coursRepository;
    @Autowired private ExerciceRepository exerciceRepository;
    @Autowired private MatiereRepository matiereRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private OptionReponseRepository optionReponseRepository;

    // ===== COURS =====

    @PostMapping("/cours")
    public ResponseEntity<?> createCours(@RequestBody CreateCoursRequest req) {
        try {
            Matiere matiere = matiereRepository.findById(req.getMatiereId())
                    .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
            Cours cours = new Cours();
            cours.setMatiere(matiere);
            cours.setTitre(req.getTitre());
            cours.setChapitre(req.getChapitre());
            cours.setNumeroChapitre(req.getNumeroChapitre());
            cours.setContenu(req.getContenu());
            cours.setResume(req.getResume());
            if (req.getType() != null) cours.setType(Cours.TypeCours.valueOf(req.getType()));
            if (req.getNiveau() != null) cours.setNiveau(Cours.Niveau.valueOf(req.getNiveau()));
            if (req.getDureeLecture() != null) cours.setDureeLecture(req.getDureeLecture());
            cours.setActif(true);
            return ResponseEntity.ok(coursRepository.save(cours));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/cours/{id}")
    public ResponseEntity<?> updateCours(@PathVariable Long id, @RequestBody CreateCoursRequest req) {
        try {
            Cours cours = coursRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
            if (req.getMatiereId() != null) {
                Matiere matiere = matiereRepository.findById(req.getMatiereId())
                        .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
                cours.setMatiere(matiere);
            }
            if (req.getTitre() != null) cours.setTitre(req.getTitre());
            if (req.getChapitre() != null) cours.setChapitre(req.getChapitre());
            if (req.getNumeroChapitre() != null) cours.setNumeroChapitre(req.getNumeroChapitre());
            if (req.getContenu() != null) cours.setContenu(req.getContenu());
            if (req.getResume() != null) cours.setResume(req.getResume());
            if (req.getType() != null) cours.setType(Cours.TypeCours.valueOf(req.getType()));
            if (req.getNiveau() != null) cours.setNiveau(Cours.Niveau.valueOf(req.getNiveau()));
            if (req.getDureeLecture() != null) cours.setDureeLecture(req.getDureeLecture());
            return ResponseEntity.ok(coursRepository.save(cours));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/cours/{id}")
    public ResponseEntity<?> deleteCours(@PathVariable Long id) {
        try {
            coursRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Cours supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== EXERCICES =====

    @PostMapping("/exercices")
    public ResponseEntity<?> createExercice(@RequestBody CreateExerciceRequest req) {
        try {
            Matiere matiere = matiereRepository.findById(req.getMatiereId())
                    .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
            Exercice ex = new Exercice();
            ex.setMatiere(matiere);
            ex.setTitre(req.getTitre());
            ex.setEnonce(req.getEnonce());
            ex.setCorrection(req.getCorrection());
            if (req.getDifficulte() != null) ex.setDifficulte(Exercice.Difficulte.valueOf(req.getDifficulte()));
            if (req.getType() != null) ex.setType(Exercice.TypeExercice.valueOf(req.getType()));
            if (req.getAnneeBac() != null) ex.setAnneeBac(req.getAnneeBac());
            if (req.getDureeMinutes() != null) ex.setDureeMinutes(req.getDureeMinutes());
            if (req.getPointsTotal() != null) ex.setPointsTotal(req.getPointsTotal());
            ex.setActif(true);
            return ResponseEntity.ok(exerciceRepository.save(ex));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/exercices/{id}")
    public ResponseEntity<?> updateExercice(@PathVariable Long id, @RequestBody CreateExerciceRequest req) {
        try {
            Exercice ex = exerciceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));
            if (req.getMatiereId() != null) {
                Matiere matiere = matiereRepository.findById(req.getMatiereId())
                        .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
                ex.setMatiere(matiere);
            }
            if (req.getTitre() != null) ex.setTitre(req.getTitre());
            if (req.getEnonce() != null) ex.setEnonce(req.getEnonce());
            if (req.getCorrection() != null) ex.setCorrection(req.getCorrection());
            if (req.getDifficulte() != null) ex.setDifficulte(Exercice.Difficulte.valueOf(req.getDifficulte()));
            if (req.getType() != null) ex.setType(Exercice.TypeExercice.valueOf(req.getType()));
            if (req.getAnneeBac() != null) ex.setAnneeBac(req.getAnneeBac());
            if (req.getDureeMinutes() != null) ex.setDureeMinutes(req.getDureeMinutes());
            if (req.getPointsTotal() != null) ex.setPointsTotal(req.getPointsTotal());
            return ResponseEntity.ok(exerciceRepository.save(ex));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/exercices/{id}")
    public ResponseEntity<?> deleteExercice(@PathVariable Long id) {
        try {
            exerciceRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Exercice supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== QUESTIONS =====

    @PostMapping("/exercices/{exerciceId}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable Long exerciceId, @RequestBody CreateQuestionRequest req) {
        try {
            Exercice exercice = exerciceRepository.findById(exerciceId)
                    .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));
            Question question = new Question();
            question.setExercice(exercice);
            question.setEnonce(req.getEnonce());
            question.setExplication(req.getExplication());
            question.setOrdre(req.getOrdre() != null ? req.getOrdre() : 1);
            question.setPoints(req.getPoints() != null ? req.getPoints() : 1.0);
            question.setOptions(new ArrayList<>());
            if (req.getOptions() != null) {
                for (CreateQuestionRequest.OptionRequest optReq : req.getOptions()) {
                    OptionReponse opt = new OptionReponse();
                    opt.setQuestion(question);
                    opt.setTexte(optReq.getTexte());
                    opt.setEstCorrecte(optReq.getEstCorrecte() != null ? optReq.getEstCorrecte() : false);
                    opt.setOrdre(optReq.getOrdre() != null ? optReq.getOrdre() : 0);
                    question.getOptions().add(opt);
                }
            }
            return ResponseEntity.ok(questionRepository.save(question));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Question supprimée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}