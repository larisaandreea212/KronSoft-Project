package com.fmi_unitbv2026.demo.repository;

import com.fmi_unitbv2026.demo.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {

    Optional<Receptionist> findByUser_IdUser(Integer idUser);
}