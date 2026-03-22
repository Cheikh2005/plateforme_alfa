package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.model.Cours;
import com.alfaconseil.bacprep.model.Matiere;
import com.alfaconseil.bacprep.model.UserProgress;
import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.CoursRepository;
import com.alfaconseil.bacprep.repository.MatiereRepository;
import com.alfaconseil.bacprep.repository.UserProgressRepository;
import com.alfaconseil.bacprep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private UserProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cours> getCoursByMatiere(Long matiereId) {
        return coursRepository.findByMatiereIdActifWithMatiere(matiereId);
    }

    @Transactional(readOnly = true)
    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findByIdWithMatiere(id);
    }

    @Transactional(readOnly = true)
    public List<Cours> getAllCours() {
        return coursRepository.findAllActifWithMatiere();
    }

    @Transactional
    public UserProgress markProgress(String username, Long coursId, Integer pourcentage) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        Optional<UserProgress> existing = progressRepository.findByUserIdAndCoursId(user.getId(), coursId);
        UserProgress progress;

        if (existing.isPresent()) {
            progress = existing.get();
            progress.setPourcentage(pourcentage);
            if (pourcentage >= 100) {
                progress.setComplete(true);
                progress.setDateCompletion(LocalDateTime.now());
            }
        } else {
            progress = new UserProgress();
            progress.setUser(user);
            progress.setCours(cours);
            progress.setPourcentage(pourcentage);
            progress.setComplete(pourcentage >= 100);
            if (pourcentage >= 100) {
                progress.setDateCompletion(LocalDateTime.now());
            }
        }
        return progressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public List<UserProgress> getUserProgress(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return progressRepository.findByUserId(user.getId());
    }
}