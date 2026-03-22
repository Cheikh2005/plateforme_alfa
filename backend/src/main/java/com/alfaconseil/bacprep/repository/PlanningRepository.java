package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PlanningRepository extends JpaRepository<Planning, Long> {
    List<Planning> findByUserIdOrderByDateDebutAsc(Long userId);
    List<Planning> findByUserIdAndDateDebutBetweenOrderByDateDebutAsc(Long userId, LocalDate debut, LocalDate fin);
    List<Planning> findByUserIdAndCompleteFalseOrderByDateDebutAsc(Long userId);
    long countByUserIdAndCompleteTrue(Long userId);
    long countByUserId(Long userId);
}