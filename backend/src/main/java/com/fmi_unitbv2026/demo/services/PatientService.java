package com.fmi_unitbv2026.demo.services;
import com.fmi_unitbv2026.demo.dto.PatientCardDTO;
import com.fmi_unitbv2026.demo.dto.PatientProfileDTO;
import com.fmi_unitbv2026.demo.entity.AIReport;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.repository.AiReportRepository;
import com.fmi_unitbv2026.demo.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fmi_unitbv2026.demo.dto.CreatePatientDTO;
import com.fmi_unitbv2026.demo.entity.Doctor;
import com.fmi_unitbv2026.demo.entity.User;
import com.fmi_unitbv2026.demo.enums.Role;
import com.fmi_unitbv2026.demo.repository.DoctorRepository;
import com.fmi_unitbv2026.demo.repository.UserRepository;
import com.fmi_unitbv2026.demo.repository.PatientResponseRepository;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AiReportRepository aiReportRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientResponseRepository patientResponseRepository;

    public PatientService(PatientRepository patientRepository,
                          AiReportRepository aiReportRepository,
                          DoctorRepository doctorRepository,
                          UserRepository userRepository,
                          PatientResponseRepository patientResponseRepository) {
        this.patientRepository = patientRepository;
        this.aiReportRepository = aiReportRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientResponseRepository = patientResponseRepository;
    }

    public List<PatientCardDTO> getPatientsForDoctor(int idDoctor) {
        List<Patient> patients = patientRepository.findByDoctor_IdDoctor(idDoctor);

        return patients.stream()
                .map(patient -> {
                    Status latestStatus = aiReportRepository
                            .findTopByPatient_IdPatientOrderByDateDesc(patient.getIdPatient())
                            .map(AIReport::getStatus)
                            .orElse(null);

                    return new PatientCardDTO(
                            String.valueOf(patient.getIdPatient()),
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getSurgeryType(),
                            latestStatus
                    );
                })
                .toList();
    }

    public List<PatientCardDTO> getCriticalPatientsForDoctor(int idDoctor) {
        List<Patient> patients = patientRepository.findByDoctor_IdDoctor(idDoctor);

        return patients.stream()
                .map(patient -> {
                    Status latestStatus = aiReportRepository
                            .findTopByPatient_IdPatientOrderByDateDesc(patient.getIdPatient())
                            .map(AIReport::getStatus)
                            .orElse(null);

                    return new PatientCardDTO(
                            String.valueOf(patient.getIdPatient()),
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getSurgeryType(),
                            latestStatus
                    );
                })
                .filter(dto -> dto.getStatus() == Status.CRITICAL)
                .toList();
    }

    public List<PatientCardDTO> searchPatientsByName(int idDoctor, String name) {
        List<Patient> patients = patientRepository
                .findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(name, name);

        return patients.stream()
                .filter(p -> p.getDoctor().getIdDoctor() == idDoctor)
                .map(patient -> {
                    Status latestStatus = aiReportRepository
                            .findTopByPatient_IdPatientOrderByDateDesc(patient.getIdPatient())
                            .map(AIReport::getStatus)
                            .orElse(null);

                    return new PatientCardDTO(
                            String.valueOf(patient.getIdPatient()),
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getSurgeryType(),
                            latestStatus
                    );
                })
                .toList();
    }

    public PatientProfileDTO getPatientProfile(int id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return new PatientProfileDTO(
                patient.getIdPatient(),
                patient.getSurgeryDate(),
                patient.getAge(),
                patient.getSex(),
                patient.getCNP()
        );
    }

    public PatientProfileDTO createPatient(CreatePatientDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getIdDoctor())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirebaseUid(dto.getFirebaseUid());
        user.setRole(Role.PATIENT);

        userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setAge(dto.getAge());
        patient.setSex(dto.getSex());
        patient.setCNP(dto.getCnp());
        patient.setSurgeryType(dto.getSurgeryType());
        patient.setSurgeryDate(dto.getSurgeryDate());
        patient.setDoctor(doctor);

        Patient savedPatient = patientRepository.save(patient);

        return new PatientProfileDTO(
                savedPatient.getIdPatient(),
                savedPatient.getSurgeryDate(),
                savedPatient.getAge(),
                savedPatient.getSex(),
                savedPatient.getCNP()
        );
    }

    public void deletePatient(int idPatient) {
        Patient patient = patientRepository.findById(idPatient)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User user = patient.getUser();

        patientResponseRepository.deleteAll(
                patientResponseRepository.findByPatient_IdPatient(idPatient)
        );

        aiReportRepository.deleteAll(
                aiReportRepository.findByPatient_IdPatientOrderByDateAsc(idPatient)
        );

        patientRepository.delete(patient);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    public PatientProfileDTO getPatientProfileByUserId(int idUser) {
        Patient patient = patientRepository.findByUser_IdUser(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found for user id: " + idUser));

        return new PatientProfileDTO(
                patient.getIdPatient(),
                patient.getSurgeryDate(),
                patient.getAge(),
                patient.getSex(),
                patient.getCNP()
        );
    }


    public PatientCardDTO getPatientCardById(Integer idPatient) {
        Patient patient = patientRepository.findById(idPatient).orElse(null);
        if (patient == null) {
            return null;
        }

        Status currentStatus = aiReportRepository.findTopByPatient_IdPatientOrderByDateDesc(idPatient)
                .map(AIReport::getStatus)
                .orElse(Status.STABLE);

        return new PatientCardDTO(
                String.valueOf(patient.getIdPatient()),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getSurgeryType(),
                currentStatus
        );
    }
}
