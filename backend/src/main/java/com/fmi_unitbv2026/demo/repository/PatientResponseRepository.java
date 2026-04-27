package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.PatientResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientResponseRepository extends JpaRepository<PatientResponse, Integer> {

    List<PatientResponse> findByPatient_IdPatient(Integer idPatient);
}
