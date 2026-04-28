package com.fmi_unitbv2026.demo.dto;

import com.fmi_unitbv2026.demo.enums.Status;

public class PatientCardDTO {
    private String idPatient;
    private String firstName;
    private String lastName;
    private String surgeryType;
    private Status status;

    public PatientCardDTO() {}

    public PatientCardDTO(String idPatient, String firstName, String lastName, String surgeryType, Status status) {
        this.idPatient = idPatient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surgeryType = surgeryType;
        this.status = status;
    }

    public String getIdPatient() {
        return idPatient;
    }
    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSurgeryType() {
        return surgeryType;
    }
    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }
}
