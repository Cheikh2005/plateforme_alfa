package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProgressRepository progressRepository;
    @Autowired
    private UserScoreRepository scoreRepository;
    @Autowired
    private PlanningRepository planningRepository;
    @Autowired
    private CoursRepository coursRepository;
    @Autowired
    private ExerciceRepository exerciceRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Map<String, Object> stats = new HashMap<>();

        // User info
        stats.put("userId", user.getId());
        stats.put("nom", user.getNom());
        stats.put("prenom", user.getPrenom());
        stats.put("serie", user.getSerie());

        // Progress stats
        long coursComplets = progressRepository.countByUserIdAndCompleteTrue(user.getId());
        long totalCours = coursRepository.countByActifTrue();
        Double avgProgress = progressRepository.avgProgressByUserId(user.getId());
        stats.put("coursComplets", coursComplets);
        stats.put("totalCours", totalCours);
        stats.put("progressionMoyenne", avgProgress != null ? Math.round(avgProgress) : 0);

        // Score stats
        long exercicesFaits = scoreRepository.countByUserId(user.getId());
        Double avgScore = scoreRepository.avgScoreByUserId(user.getId());
        stats.put("exercicesFaits", exercicesFaits);
        stats.put("scoreMoyen", avgScore != null ? Math.round(avgScore) : 0);

        // Planning stats
        long tasksDone = planningRepository.countByUserIdAndCompleteTrue(user.getId());
        long totalTasks = planningRepository.countByUserId(user.getId());
        stats.put("tasksDone", tasksDone);
        stats.put("totalTasks", totalTasks);

        // Total exercises
        stats.put("totalExercices", exerciceRepository.countByActifTrue());

        // Recent scores (without questions to avoid lazy loading)
        var recentScores = scoreRepository.findByUserId(user.getId(),
                org.springframework.data.domain.PageRequest.of(0, 5)).getContent();
        recentScores.forEach(s -> s.getExercice().setQuestions(null));
        stats.put("recentScores", recentScores);

        // Upcoming plannings
        stats.put("upcomingPlannings", planningRepository.findByUserIdAndCompleteFalseOrderByDateDebutAsc(user.getId())
                .stream().limit(5).toList());

        return stats;
    }
}