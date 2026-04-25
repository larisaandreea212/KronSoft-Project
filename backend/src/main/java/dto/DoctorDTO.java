package dto;

public class DoctorDTO {
    private String idDoctor;
    private String firstName;
    private String lastName;
    private String specialization;
    private String hospitalName;

    public DoctorDTO(String idDoctor, String firstName, String lastName, String specialization, String hospitalName) {
        this.idDoctor = idDoctor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.hospitalName = hospitalName;
    }


    public String getIdDoctor() {
        return idDoctor;
    }
    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getHospitalName() {
        return hospitalName;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
