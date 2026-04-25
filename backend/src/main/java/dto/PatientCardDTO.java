package dto;

public class PatientCardDTO {
    private String idPatient;
    private String firstName;
    private String lastName;
    private String surgeryType;
    private String status;


    public PatientCardDTO(String idPatient, String firstName, String lastName, String surgeryType, String status) {
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurgeryType() {
        return surgeryType;
    }
    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }
}
