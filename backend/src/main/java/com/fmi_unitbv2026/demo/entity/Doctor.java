package com.fmi_unitbv2026.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private int idDoctor;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "is_active")
    private boolean isActive = true;

    public Doctor() { }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
