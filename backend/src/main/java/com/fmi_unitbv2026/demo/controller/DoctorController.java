package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.dto.CreateDoctorDTO;
import com.fmi_unitbv2026.demo.dto.DeactivateDoctorDTO;
import com.fmi_unitbv2026.demo.dto.DoctorDTO;
import com.fmi_unitbv2026.demo.entity.Doctor;
import com.fmi_unitbv2026.demo.repository.DoctorRepository;
import com.fmi_unitbv2026.demo.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private DoctorRepository doctorRepository;

    public DoctorController(DoctorService doctorService, DoctorRepository doctorRepository) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{idDoctor}")
    public ResponseEntity<DoctorDTO> getDoctorInfo(@PathVariable int idDoctor)
    {
        DoctorDTO doctorDTO = doctorService.getDoctorById(idDoctor);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody CreateDoctorDTO dto) {
        DoctorDTO createdDoctor = doctorService.createDoctor(dto);
        return ResponseEntity.ok(createdDoctor);
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateDoctor(@RequestBody DeactivateDoctorDTO dto) {
        doctorService.deactivateDoctor(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<DoctorDTO>> getActiveDoctors() {

        return ResponseEntity.ok(
                doctorService.getActiveDoctors()
        );
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<DoctorDTO>> getInactiveDoctors() {

        return ResponseEntity.ok(
                doctorService.getInactiveDoctors()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Doctor> getDoctorByUserId(@PathVariable Integer userId) {
        return doctorRepository.findByUser_IdUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
