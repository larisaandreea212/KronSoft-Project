// File: src/main/java/com/fmi_unitbv2026/demo/controller/PatientController.java
package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.PatientCardDTO;
import com.fmi_unitbv2026.demo.dto.PatientProfileDTO;
import com.fmi_unitbv2026.demo.dto.PatientSummaryDTO;
import com.fmi_unitbv2026.demo.dto.QuestionResponseDTO;
import com.fmi_unitbv2026.demo.enums.ResponseType;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/summary")
    public ResponseEntity<PatientSummaryDTO> getSummary() {
        // sample data
        QuestionResponseDTO q1 = new QuestionResponseDTO("Cum te simți?", "Bine", ResponseType.YES_NO);
        QuestionResponseDTO q2 = new QuestionResponseDTO("Durere la scara 1-5?", "3", ResponseType.SCALE_1_5);

        PatientSummaryDTO dto = new PatientSummaryDTO(
                85,
                List.of(q1, q2),
                Status.STABLE,
                "Evoluție favorabilă"
        );

        return ResponseEntity.ok(dto);
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
        List<PatientCardDTO> listCriticalPatientsDTO = patientService.getCriticalPatientsForDoctor(idDoctor);
        return ResponseEntity.ok(listCriticalPatientsDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientCardDTO>> searchPatients( @RequestParam int idDoctor, @RequestParam String name)
    {
        List<PatientCardDTO> patientCards = patientService.searchPatientsByName(idDoctor, name);
        return ResponseEntity.ok(patientCards);
    }

    //aici am modificat sa primeasca IDPatient, nu IdDoctor deoarece metoda din servide primeste IdPatient
    @GetMapping("/profile/{idPatient}")
    public ResponseEntity<PatientProfileDTO> getPatientProfile(@PathVariable int idPatient)
    {
        PatientProfileDTO patientProfileDTO = patientService.getPatientProfile(idPatient);
        return ResponseEntity.ok(patientProfileDTO);
    }

}
