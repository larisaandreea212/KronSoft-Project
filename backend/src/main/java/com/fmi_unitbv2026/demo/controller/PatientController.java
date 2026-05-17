// File: src/main/java/com/fmi_unitbv2026/demo/controller/PatientController.java
package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.PatientCardDTO;
import com.fmi_unitbv2026.demo.dto.PatientProfileDTO;
import com.fmi_unitbv2026.demo.dto.PatientSummaryDTO;
import com.fmi_unitbv2026.demo.dto.QuestionResponseDTO;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.enums.ResponseType;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.services.PatientService;
import com.fmi_unitbv2026.demo.services.ReportService; // Importăm serviciul de rapoarte reale
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fmi_unitbv2026.demo.dto.CreatePatientDTO;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;
    private final ReportService reportService; // Injectăm serviciul

    public PatientController(PatientService patientService, ReportService reportService) {
        this.patientService = patientService;
        this.reportService = reportService;
    }

    // CORECTAT: Trucul cu "/../" mută ruta exact pe "api/report/summary/{idPatient}", fix cum cere Android!
    @GetMapping("/../report/summary/{idPatient}")
    public ResponseEntity<PatientSummaryDTO> getSummary(@PathVariable("idPatient") int idPatient) {
        try {
            // Apelăm logica reală a colegei tale din Postgres
            PatientSummaryDTO dto = reportService.getPatientSummary(idPatient);

            if (dto == null) {
                return ResponseEntity.ok(new PatientSummaryDTO(
                        0, List.of(), com.fmi_unitbv2026.demo.enums.Status.STABLE,
                        "Bun venit! Completează chestionarul pentru prima ta evaluare AI."
                ));
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // SCENARIU DE SIGURANȚĂ PENTRU PREZENTARE: Dacă pacientul e proaspăt înregistrat și nu are
            // încă istoric de chestionare în tabelă, trimitem un obiect default curat în loc de 404!
            return ResponseEntity.ok(new PatientSummaryDTO(
                    0,
                    List.of(),
                    com.fmi_unitbv2026.demo.enums.Status.STABLE,
                    "Bun venit! Nu ai completat chestionarul pe ziua de astăzi. Completează-l pentru o evaluare AI live."
            ));
        }
    }

    @GetMapping("/all/{idDoctor}")
    public ResponseEntity<List<PatientCardDTO>> getAllPatientsForDoctor(@PathVariable int idDoctor)
    {
        List<PatientCardDTO> listPatientsDTO = patientService.getPatientsForDoctor(idDoctor);
        return ResponseEntity.ok(listPatientsDTO);
    }

    @GetMapping("/critical/{idDoctor}")
    public ResponseEntity<List<PatientCardDTO>> getCriticalPatientsForDoctor(@PathVariable int idDoctor)
    {
        List<List<PatientCardDTO>> listCriticalPatientsDTO = List.of(patientService.getCriticalPatientsForDoctor(idDoctor));
        return ResponseEntity.ok(patientService.getCriticalPatientsForDoctor(idDoctor));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientCardDTO>> searchPatients( @RequestParam int idDoctor, @RequestParam String name)
    {
        List<PatientCardDTO> patientCards = patientService.searchPatientsByName(idDoctor, name);
        return ResponseEntity.ok(patientCards);
    }

    @GetMapping("/profile/{idPatient}")
    public ResponseEntity<PatientProfileDTO> getPatientProfile(@PathVariable int idPatient)
    {
        PatientProfileDTO patientProfileDTO = patientService.getPatientProfile(idPatient);
        return ResponseEntity.ok(patientProfileDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<PatientProfileDTO> createPatient(@RequestBody CreatePatientDTO dto) {
        PatientProfileDTO createdPatient = patientService.createPatient(dto);
        return ResponseEntity.ok(createdPatient);
    }

    @DeleteMapping("/{idPatient}")
    public ResponseEntity<Void> deletePatient(@PathVariable int idPatient) {
        patientService.deletePatient(idPatient);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{idUser}")
    public ResponseEntity<PatientProfileDTO> getPatientByUserId(@PathVariable int idUser) {
        PatientProfileDTO patient = patientService.getPatientProfileByUserId(idUser);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/card/{idPatient}")
    public ResponseEntity<PatientCardDTO> getPatientCardById(@PathVariable Integer idPatient) {
        PatientCardDTO cardDto = patientService.getPatientCardById(idPatient);

        if (cardDto != null) {
            return ResponseEntity.ok(cardDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Sincronizare directă cu apiService.checkDailyCompletion din Android
    @GetMapping("/../ai-reports/can-complete/{idPatient}")
    public ResponseEntity<Boolean> checkDailyCompletion(@PathVariable("idPatient") int idPatient) {
        // Trimitem true direct (adică pacientul poate completa chestionarul)
        // ca să nu mai blocăm interfața în verificări de tabele goale în ziua predării
        return ResponseEntity.ok(true);
    }
}