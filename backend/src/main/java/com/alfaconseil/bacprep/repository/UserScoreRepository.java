package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    List<UserScore> findByUserIdOrderByDatePassageDesc(Long userId);
    org.springframework.data.domain.Page<UserScore> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT AVG(us.score / us.scoreMax * 100) FROM UserScore us WHERE us.user.id = :userId")
    Double avgScoreByUserId(Long userId);

    long countByUserId(Long userId);
}