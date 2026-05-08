package com.fmi_unitbv2026.demo.dto;

public class CreatePatientDTO {

    private String email;
    private String firebaseUid;
    private String firstName;
    private String lastName;
    private String sex;
    private String cnp;
    private String surgeryType;
    private int age;
    private int idDoctor;

    public CreatePatientDTO() {}

    public CreatePatientDTO(int idDoctor, int age, String surgeryType, String cnp, String sex,
                            String lastName, String firstName, String firebaseUid, String email) {
        this.idDoctor = idDoctor;
        this.age = age;
        this.surgeryType = surgeryType;
        this.cnp = cnp;
        this.sex = sex;
        this.lastName = lastName;
        this.firstName = firstName;
        this.firebaseUid = firebaseUid;
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }

    public String getSurgeryType() { return surgeryType; }
    public void setSurgeryType(String surgeryType) { this.surgeryType = surgeryType; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }
}
