package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.EvolutionDTO;
import com.fmi_unitbv2026.demo.dto.PatientSummaryDTO;
import com.fmi_unitbv2026.demo.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/evolution/{idPatient}")
    public ResponseEntity<List<EvolutionDTO>> getEvolution(@PathVariable int idPatient)
    {
        List<EvolutionDTO> evolution = reportService.getPatientEvolution(idPatient);
        return ResponseEntity.ok(evolution);
    }

    @GetMapping("/summary/{idPatient}")
    public ResponseEntity<PatientSummaryDTO> getPatientSummary(@PathVariable int idPatient)
    {
        PatientSummaryDTO patientSummary = reportService.getPatientSummary(idPatient);
        return ResponseEntity.ok(patientSummary);
    }
}
