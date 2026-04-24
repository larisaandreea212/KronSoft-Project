package com.fmi_unitbv2026.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private int idPatient;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "sex")
    private String sex;

    @Column(name = "CNP")
    private String CNP;

    @Column(name = "surgery_type")
    private String surgeryType;

    @Column(name = "surgery_date")
    private LocalDate surgeryDate;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;
}
