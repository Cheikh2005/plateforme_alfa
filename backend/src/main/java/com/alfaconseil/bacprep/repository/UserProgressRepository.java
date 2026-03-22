package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    Optional<UserProgress> findByUserIdAndCoursId(Long userId, Long coursId);
    long countByUserIdAndCompleteTrue(Long userId);

    @Query("SELECT AVG(up.pourcentage) FROM UserProgress up WHERE up.user.id = :userId")
    Double avgProgressByUserId(Long userId);
}