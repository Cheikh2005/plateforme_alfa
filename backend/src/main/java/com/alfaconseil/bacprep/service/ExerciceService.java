package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.dto.SubmitAnswerRequest;
import com.alfaconseil.bacprep.model.*;
import com.alfaconseil.bacprep.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ExerciceService {

    @Autowired
    private ExerciceRepository exerciceRepository;

    @Autowired
    private UserScoreRepository userScoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Exercice> getAllExercices() {
        List<Exercice> exercices = exerciceRepository.findAllActifWithMatiere();
        exercices.forEach(e -> e.setQuestions(null));
        return exercices;
    }

    @Transactional(readOnly = true)
    public List<Exercice> getExercicesByMatiere(Long matiereId) {
        List<Exercice> exercices = exerciceRepository.findByMatiereIdActifWithMatiere(matiereId);
        exercices.forEach(e -> e.setQuestions(null));
        return exercices;
    }

    @Transactional(readOnly = true)
    public List<Exercice> getExercicesByType(String type) {
        List<Exercice> exercices = exerciceRepository.findByTypeWithMatiere(
                Exercice.TypeExercice.valueOf(type.toUpperCase()));
        exercices.forEach(e -> e.setQuestions(null));
        return exercices;
    }

    @Transactional(readOnly = true)
    public List<Exercice> getExercicesByMatiereAndType(Long matiereId, String type) {
        Exercice.TypeExercice typeEnum = Exercice.TypeExercice.valueOf(type.toUpperCase());
        List<Exercice> exercices = exerciceRepository.findByMatiereIdActifWithMatiere(matiereId).stream()
                .filter(e -> e.getType() == typeEnum)
                .toList();
        exercices.forEach(e -> e.setQuestions(null));
        return exercices;
    }

    @Transactional(readOnly = true)
    public Optional<Exercice> getExerciceById(Long id) {
        List<Exercice> results = exerciceRepository.findByIdWithQuestionsAndOptions(id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Map<String, Object> submitAnswers(String username, SubmitAnswerRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Exercice exercice = exerciceRepository.findById(request.getExerciceId())
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));

        int totalQuestions = exercice.getQuestions() != null ? exercice.getQuestions().size() : 0;
        int correctAnswers = 0;

        Map<Long, Boolean> resultats = new HashMap<>();

        if (exercice.getQuestions() != null) {
            for (Question question : exercice.getQuestions()) {
                Long selectedOptionId = request.getReponses() != null ?
                        request.getReponses().get(question.getId()) : null;
                boolean isCorrect = false;

                if (selectedOptionId != null && question.getOptions() != null) {
                    for (OptionReponse option : question.getOptions()) {
                        if (option.getId().equals(selectedOptionId) && option.getEstCorrecte()) {
                            isCorrect = true;
                            correctAnswers++;
                            break;
                        }
                    }
                }
                resultats.put(question.getId(), isCorrect);
            }
        }

        double score = totalQuestions > 0 ?
                (double) correctAnswers / totalQuestions * exercice.getPointsTotal() : 0;

        UserScore userScore = new UserScore();
        userScore.setUser(user);
        userScore.setExercice(exercice);
        userScore.setScore(score);
        userScore.setScoreMax((double) exercice.getPointsTotal());
        userScore.setTempsPasseMinutes(request.getTempsPasseMinutes());
        userScoreRepository.save(userScore);

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("scoreMax", exercice.getPointsTotal());
        result.put("pourcentage", totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0);
        result.put("correctAnswers", correctAnswers);
        result.put("totalQuestions", totalQuestions);
        result.put("resultats", resultats);
        result.put("mention", getMention(totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0));

        return result;
    }

    private String getMention(double pourcentage) {
        if (pourcentage >= 90) return "Excellent ! 🏆";
        if (pourcentage >= 75) return "Très Bien ! 🌟";
        if (pourcentage >= 60) return "Bien ! 👍";
        if (pourcentage >= 50) return "Passable 📚";
        return "À revoir 💪";
    }

    @Transactional(readOnly = true)
    public List<UserScore> getUserScores(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        List<UserScore> scores = userScoreRepository.findByUserIdOrderByDatePassageDesc(user.getId());
        scores.forEach(s -> s.getExercice().setQuestions(null));
        return scores;
    }
}