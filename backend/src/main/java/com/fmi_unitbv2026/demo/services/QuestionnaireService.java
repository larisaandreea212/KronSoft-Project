package com.fmi_unitbv2026.demo.services;

import com.fmi_unitbv2026.demo.dto.QuestionsDTO;
import com.fmi_unitbv2026.demo.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public List<QuestionsDTO> getQuestions() {

        return questionnaireRepository.findAll()
                .stream()
                .map(questionnaire -> new QuestionsDTO(
                        questionnaire.getIdQuestion(),
                        questionnaire.getResponseType(),
                        questionnaire.getQuestionText()
                ))
                .collect(Collectors.toList());
    }
}