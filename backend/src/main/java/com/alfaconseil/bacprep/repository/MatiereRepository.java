package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    Optional<Matiere> findByCode(String code);
    boolean existsByCode(String code);
}