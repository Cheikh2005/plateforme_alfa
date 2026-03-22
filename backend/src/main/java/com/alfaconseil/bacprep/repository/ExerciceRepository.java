package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.Exercice;
import com.alfaconseil.bacprep.model.Exercice.TypeExercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ExerciceRepository extends JpaRepository<Exercice, Long> {

    @Query("SELECT e FROM Exercice e JOIN FETCH e.matiere WHERE e.actif = true ORDER BY e.matiere.id ASC, e.dateCreation DESC")
    List<Exercice> findAllActifWithMatiere();

    @Query("SELECT e FROM Exercice e JOIN FETCH e.matiere WHERE e.matiere.id = :matiereId AND e.actif = true ORDER BY e.dateCreation DESC")
    List<Exercice> findByMatiereIdActifWithMatiere(@Param("matiereId") Long matiereId);

    @Query("SELECT e FROM Exercice e JOIN FETCH e.matiere WHERE e.type = :type AND e.actif = true ORDER BY e.dateCreation DESC")
    List<Exercice> findByTypeWithMatiere(@Param("type") TypeExercice type);

    @Query("SELECT DISTINCT e FROM Exercice e JOIN FETCH e.matiere LEFT JOIN FETCH e.questions q LEFT JOIN FETCH q.options WHERE e.id = :id")
    List<Exercice> findByIdWithQuestionsAndOptions(@Param("id") Long id);

    long countByActifTrue();
}
