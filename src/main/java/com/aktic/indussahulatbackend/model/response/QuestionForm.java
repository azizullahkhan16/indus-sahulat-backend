package com.aktic.indussahulatbackend.model.response;
import lombok.Data;

import java.util.List;

@Data
public class QuestionForm {
    Long questionId;
    String Question;
    List<String> options;

}