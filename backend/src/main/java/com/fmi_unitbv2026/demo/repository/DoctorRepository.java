package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository  extends JpaRepository<Doctor, Integer> {
}
