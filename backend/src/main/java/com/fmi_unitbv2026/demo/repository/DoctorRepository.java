package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository  extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByUser_IdUser(Integer idUser);

    List<Doctor> findByIsActiveTrue();

    List<Doctor> findByIsActiveFalse();
}
