package com.fmi_unitbv2026.demo.dto;

public class CreateDoctorDTO {

    private String email;
    private String firebaseUid;
    private String firstName;
    private String lastName;
    private String specialization;
    private String hospitalName;

    public CreateDoctorDTO() {}

    public CreateDoctorDTO(String email, String firebaseUid, String firstName, String lastName,
                           String specialization, String hospitalName) {
        this.email = email;
        this.firebaseUid = firebaseUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.hospitalName = hospitalName;
    }

    public String getHospitalName() { return hospitalName; }

    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getSpecialization() { return specialization; }

    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFirebaseUid() { return firebaseUid; }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
