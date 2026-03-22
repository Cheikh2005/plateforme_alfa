package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}