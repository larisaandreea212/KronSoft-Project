package com.fmi_unitbv2026.demo.services;

import com.fmi_unitbv2026.demo.dto.EvolutionDTO;
import com.fmi_unitbv2026.demo.dto.PatientSummaryDTO;
import com.fmi_unitbv2026.demo.dto.QuestionResponseDTO;
import com.fmi_unitbv2026.demo.entity.AIReport;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.repository.AiReportRepository;
import com.fmi_unitbv2026.demo.repository.PatientRepository;
import com.fmi_unitbv2026.demo.repository.PatientResponseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final AiReportRepository aiReportRepository;
    private final PatientRepository patientRepository;
    private final PatientResponseRepository patientResponseRepository;

    public ReportService(AiReportRepository aiReportRepository,
                         PatientRepository patientRepository, PatientResponseRepository patientResponseRepository) {
        this.aiReportRepository = aiReportRepository;
        this.patientRepository = patientRepository;
        this.patientResponseRepository = patientResponseRepository;
    }

    public AIReport generateReport(int idPatient, List<Integer> answersScores) {
        Patient patient = patientRepository.findById(idPatient)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        int score = answersScores.stream()
                .mapToInt(Integer::intValue)
                .sum();

        String notes = generateNotes(score);

        AIReport report = new AIReport();
        report.setPatient(patient);
        report.setDate(LocalDate.now());
        report.setAiScore(score);
        report.setAiNote(notes);

        return aiReportRepository.save(report);
    }

    public List<EvolutionDTO> getPatientEvolution(int idPatient) {
        return aiReportRepository.findByPatient_IdPatientOrderByDateAsc(idPatient)
                .stream()
                .map(report -> new EvolutionDTO(
                        report.getDate(),
                        report.getAiScore()
                ))
                .toList();
    }

    public PatientSummaryDTO getPatientSummary(int idPatient) {
        AIReport latestReport = aiReportRepository
                .findTopByPatient_IdPatientOrderByDateDesc(idPatient)
                .orElseThrow(() -> new RuntimeException("No AI report found"));

        List<QuestionResponseDTO> questionResponses = patientResponseRepository
                .findByPatient_IdPatient(idPatient)
                .stream()
                .map(response -> new QuestionResponseDTO(
                        response.getQuestion().getQuestionText(),
                        response.getAnswerText(),
                        response.getQuestion().getResponseType()
                ))
                .toList();

        return new PatientSummaryDTO(
                latestReport.getAiScore(),
                questionResponses,
                latestReport.getStatus(),
                latestReport.getAiNote()
        );
    }

    private String generateNotes(int score) {
        if (score >= 80) {
            return "Pacientul prezinta risc critic. Este recomandata verificare medicala urgenta.";
        } else if (score >= 50) {
            return "Pacientul prezinta semne de agravare. Este recomandata monitorizare atenta.";
        } else {
            return "Pacientul pare stabil conform raspunsurilor introduse.";
        }
    }
}