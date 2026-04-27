package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.AIReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiReportRepository extends JpaRepository<AIReport, Integer> {

    List<AIReport> findByPatient_IdPatientOrderByDateAsc(Integer idPatient);

    Optional<AIReport> findTopByPatient_IdPatientOrderByDateDesc(Integer idPatient);
}
