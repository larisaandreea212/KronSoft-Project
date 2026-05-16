package com.fmi_unitbv2026.demo.services;

import com.fmi_unitbv2026.demo.dto.AnswerDTO;
import com.fmi_unitbv2026.demo.dto.QuestionnaireSubmissionDTO;
import com.fmi_unitbv2026.demo.entity.AIReport;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.entity.PatientResponse;
import com.fmi_unitbv2026.demo.entity.Questionnaire;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.repository.AiReportRepository;
import com.fmi_unitbv2026.demo.repository.PatientRepository;
import com.fmi_unitbv2026.demo.repository.PatientResponseRepository;
import com.fmi_unitbv2026.demo.repository.QuestionnaireRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIReportService {

    private final PatientRepository patientRepository;
    private final PatientResponseRepository patientResponseRepository;
    private final AiReportRepository aiReportRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final AIScoringService aiScoringService;

    public AIReportService(
            PatientRepository patientRepository,
            PatientResponseRepository patientResponseRepository,
            AiReportRepository aiReportRepository,
            QuestionnaireRepository questionnaireRepository,
            AIScoringService aiScoringService) {

        this.patientRepository = patientRepository;
        this.patientResponseRepository = patientResponseRepository;
        this.aiReportRepository = aiReportRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.aiScoringService = aiScoringService;
    }


    public boolean canPatientCompleteToday(int idPatient) {
        return !aiReportRepository.existsByPatient_IdPatientAndDate(idPatient, LocalDate.now());
    }

    @Transactional
    public AIReport submitQuestionnaire(QuestionnaireSubmissionDTO submissionDTO) {

        int idPatient = submissionDTO.getIdPatient();

        if (!canPatientCompleteToday(idPatient)) {
            throw new IllegalStateException("Patient already completed the questionnaire today.");
        }

        Patient patient = patientRepository.findById(idPatient)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + idPatient));

        List<PatientResponse> responses = new ArrayList<>();

        for (AnswerDTO answerDTO : submissionDTO.getAnswers()) {

            Questionnaire questionnaire = questionnaireRepository.findById(answerDTO.getIdQuestion())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + answerDTO.getIdQuestion()));

            PatientResponse response = new PatientResponse();
            response.setPatient(patient);
            response.setQuestion(questionnaire);
            response.setAnswerText(answerDTO.getAnswerText());

            responses.add(response);
        }

        patientResponseRepository.saveAll(responses);

        int score = aiScoringService.calculateScore(responses);
        Status status = aiScoringService.determineStatus(score);
        String note = aiScoringService.generateNote(responses, score, status);

        AIReport report = new AIReport();
        report.setPatient(patient);
        report.setAiScore(score);
        report.setStatus(status);
        report.setAiNote(note);
        report.setDate(LocalDate.now());

        return aiReportRepository.save(report);
    }

    public java.util.Optional<AIReport> getLastReport(int idPatient) {
        return aiReportRepository.findTopByPatient_IdPatientOrderByDateDesc(idPatient);
    }
}