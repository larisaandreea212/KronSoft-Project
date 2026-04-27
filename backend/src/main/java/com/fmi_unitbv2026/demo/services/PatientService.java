package com.fmi_unitbv2026.demo.services;
import com.fmi_unitbv2026.demo.dto.PatientCardDTO;
import com.fmi_unitbv2026.demo.dto.PatientProfileDTO;
import com.fmi_unitbv2026.demo.entity.AIReport;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.repository.AiReportRepository;
import com.fmi_unitbv2026.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AiReportRepository aiReportRepository;

    public PatientService(PatientRepository patientRepository,
                          AiReportRepository aiReportRepository) {
        this.patientRepository = patientRepository;
        this.aiReportRepository = aiReportRepository;
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
}
