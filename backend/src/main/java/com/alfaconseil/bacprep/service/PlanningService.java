package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.model.Planning;
import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.MatiereRepository;
import com.alfaconseil.bacprep.repository.PlanningRepository;
import com.alfaconseil.bacprep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlanningService {

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    public Planning createPlanning(String username, Planning planning) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        planning.setUser(user);
        return planningRepository.save(planning);
    }

    public List<Planning> getUserPlannings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return planningRepository.findByUserIdOrderByDateDebutAsc(user.getId());
    }

    public List<Planning> getPlanningsByWeek(String username, LocalDate debut, LocalDate fin) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return planningRepository.findByUserIdAndDateDebutBetweenOrderByDateDebutAsc(user.getId(), debut, fin);
    }

    public Planning updatePlanning(Long id, String username, Planning updates) {
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planning non trouvé"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!planning.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Non autorisé");
        }

        if (updates.getTitre() != null) planning.setTitre(updates.getTitre());
        if (updates.getDescription() != null) planning.setDescription(updates.getDescription());
        if (updates.getDateDebut() != null) planning.setDateDebut(updates.getDateDebut());
        if (updates.getDateFin() != null) planning.setDateFin(updates.getDateFin());
        if (updates.getCouleur() != null) planning.setCouleur(updates.getCouleur());
        if (updates.getComplete() != null) planning.setComplete(updates.getComplete());

        return planningRepository.save(planning);
    }

    public void deletePlanning(Long id, String username) {
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planning non trouvé"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!planning.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Non autorisé");
        }
        planningRepository.delete(planning);
    }

    public Planning toggleComplete(Long id, String username) {
        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planning non trouvé"));
        planning.setComplete(!planning.getComplete());
        return planningRepository.save(planning);
    }
}