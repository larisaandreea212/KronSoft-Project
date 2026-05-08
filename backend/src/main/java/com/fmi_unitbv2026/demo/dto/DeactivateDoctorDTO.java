package com.fmi_unitbv2026.demo.dto;

public class DeactivateDoctorDTO {

    private int idDoctorToDeactivate;
    private int idDoctorToReceivePatient;

    public DeactivateDoctorDTO() {}

    public DeactivateDoctorDTO(int idDoctorToDeactivate, int idDoctorToReceivePatient) {
        this.idDoctorToDeactivate = idDoctorToDeactivate;
        this.idDoctorToReceivePatient = idDoctorToReceivePatient;
    }

    public int getIdDoctorToDeactivate() { return idDoctorToDeactivate; }

    public void setIdDoctorToDeactivate(int idDoctorToDeactivate) { this.idDoctorToDeactivate = idDoctorToDeactivate; }

    public int getIdDoctorToReceivePatient() { return idDoctorToReceivePatient; }

    public void setIdDoctorToReceivePatient(int idDoctorToReceivePatient) { this.idDoctorToReceivePatient = idDoctorToReceivePatient; }
}