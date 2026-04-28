// File: src/main/java/com/fmi_unitbv2026/demo/controller/PatientController.java
package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.PatientSummaryDTO;
import com.fmi_unitbv2026.demo.dto.QuestionResponseDTO;
import com.fmi_unitbv2026.demo.enums.ResponseType;
import com.fmi_unitbv2026.demo.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*") // dev only; tighten later
public class PatientController {

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
}
