package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.QuestionsDTO;
import com.fmi_unitbv2026.demo.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionnaireService.getQuestions());
    }
}