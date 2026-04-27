package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacientRepository extends JpaRepository<Patient, Integer> {

    List<Patient> findAllByIdDoctor(int idDoctor);

    List<Patient> findByStatus(Status status);

    List<Patient> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String lastName, String firstName);
}
