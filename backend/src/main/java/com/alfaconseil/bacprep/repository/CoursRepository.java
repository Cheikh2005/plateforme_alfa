package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours, Long> {

    @Query("SELECT c FROM Cours c JOIN FETCH c.matiere WHERE c.actif = true ORDER BY c.matiere.id ASC, c.numeroChapitre ASC")
    List<Cours> findAllActifWithMatiere();

    @Query("SELECT c FROM Cours c JOIN FETCH c.matiere WHERE c.matiere.id = :matiereId AND c.actif = true ORDER BY c.numeroChapitre ASC")
    List<Cours> findByMatiereIdActifWithMatiere(@Param("matiereId") Long matiereId);

    @Query("SELECT c FROM Cours c JOIN FETCH c.matiere WHERE c.id = :id AND c.actif = true")
    Optional<Cours> findByIdWithMatiere(@Param("id") Long id);

    long countByActifTrue();
}
