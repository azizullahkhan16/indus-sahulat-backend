package com.aktic.indussahulatbackend.repository.question;

import com.aktic.indussahulatbackend.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

}