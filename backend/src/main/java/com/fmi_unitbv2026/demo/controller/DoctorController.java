package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.DoctorDTO;
import com.fmi_unitbv2026.demo.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{idDoctor}")
    public ResponseEntity<DoctorDTO> getDoctorInfo(@PathVariable int idDoctor)
    {
        DoctorDTO doctorDTO = doctorService.getDoctorById(idDoctor);
        return ResponseEntity.ok(doctorDTO);
    }
}
