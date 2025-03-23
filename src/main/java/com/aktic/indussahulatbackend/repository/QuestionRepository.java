package com.aktic.indussahulatbackend.repository;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

}