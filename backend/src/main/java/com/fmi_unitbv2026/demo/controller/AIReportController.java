package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.QuestionnaireSubmissionDTO;
import com.fmi_unitbv2026.demo.entity.AIReport;
import com.fmi_unitbv2026.demo.services.AIReportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-reports")
public class AIReportController {

    private final AIReportService aiReportService;

    public AIReportController(AIReportService aiReportService) {
        this.aiReportService = aiReportService;
    }

    @PostMapping("/submit")
    public ResponseEntity<AIReport> submitQuestionnaire(@RequestBody QuestionnaireSubmissionDTO submissionDTO) {
        AIReport report = aiReportService.submitQuestionnaire(submissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/can-complete/{idPatient}")
    public ResponseEntity<Boolean> canPatientCompleteToday(@PathVariable int idPatient) {
        boolean canComplete = aiReportService.canPatientCompleteToday(idPatient);
        return ResponseEntity.ok(canComplete);
    }

    @GetMapping("/last/{idPatient}")
    public ResponseEntity<AIReport> getLastReport(@PathVariable int idPatient) {
        return aiReportService.getLastReport(idPatient)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}