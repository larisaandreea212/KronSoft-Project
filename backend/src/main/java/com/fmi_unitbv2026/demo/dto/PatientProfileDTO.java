package com.fmi_unitbv2026.demo.dto;

import java.time.LocalDate;

public class PatientProfileDTO {
    private int idPatient;
    private String cnp;
    private String sex;
    private int age;
    private LocalDate surgeryDate;

    public PatientProfileDTO(int idPatient, LocalDate surgeryDate, int age, String sex, String cnp) {
        this.idPatient = idPatient;
        this.surgeryDate = surgeryDate;
        this.age = age;
        this.sex = sex;
        this.cnp = cnp;
    }


    public int getIdPatient() {
        return idPatient;
    }
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getCnp() {
        return cnp;
    }
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getSurgeryDate() {
        return surgeryDate;
    }
    public void setSurgeryDate(LocalDate surgeryDate) {
        this.surgeryDate = surgeryDate;
    }
}
