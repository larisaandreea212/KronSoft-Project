package com.fmi_unitbv2026.demo.services;
import com.fmi_unitbv2026.demo.dto.CreateDoctorDTO;
import com.fmi_unitbv2026.demo.dto.DeactivateDoctorDTO;
import com.fmi_unitbv2026.demo.dto.DoctorDTO;
import com.fmi_unitbv2026.demo.entity.Doctor;
import com.fmi_unitbv2026.demo.entity.Patient;
import com.fmi_unitbv2026.demo.entity.User;
import com.fmi_unitbv2026.demo.enums.Role;
import com.fmi_unitbv2026.demo.repository.DoctorRepository;
import com.fmi_unitbv2026.demo.repository.PatientRepository;
import com.fmi_unitbv2026.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    public DoctorDTO getDoctorById(int id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return new DoctorDTO(
                doctor.getIdDoctor(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getHospitalName(),
                doctor.isActive()
        );
    }

    public DoctorDTO createDoctor(CreateDoctorDTO dto) {

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setFirebaseUid(dto.getFirebaseUid());
        user.setRole(Role.DOCTOR);

        userRepository.save(user);

        Doctor doctor = new Doctor();

        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setHospitalName(dto.getHospitalName());

        doctor.setUser(user);

        doctorRepository.save(doctor);

        return new DoctorDTO(
                doctor.getIdDoctor(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getHospitalName(),
                doctor.isActive()
        );
    }

    public void deactivateDoctor(DeactivateDoctorDTO dto) {
        Doctor doctorToDeactivate = doctorRepository
                .findById(dto.getIdDoctorToDeactivate())
                .orElseThrow(() -> new RuntimeException("Doctor to deactivate not found"));

        Doctor doctorToReceivePatients = doctorRepository
                .findById(dto.getIdDoctorToReceivePatient())
                .orElseThrow(() -> new RuntimeException("Doctor to receive patients not found"));

        List<Patient> patients = patientRepository
                .findByDoctor_IdDoctor(doctorToDeactivate.getIdDoctor());

        for (Patient patient : patients) {
            patient.setDoctor(doctorToReceivePatients);
        }

        patientRepository.saveAll(patients);

        doctorToDeactivate.setActive(false);
        doctorRepository.save(doctorToDeactivate);
    }

    public List<DoctorDTO> getActiveDoctors() {

        List<Doctor> doctors = doctorRepository.findByIsActiveTrue();

        return doctors.stream()
                .map(doctor -> new DoctorDTO(
                        doctor.getIdDoctor(),
                        doctor.getFirstName(),
                        doctor.getLastName(),
                        doctor.getSpecialization(),
                        doctor.getHospitalName(),
                        doctor.isActive()
                ))
                .toList();
    }

    public List<DoctorDTO> getInactiveDoctors() {

        List<Doctor> doctors = doctorRepository.findByIsActiveFalse();

        return doctors.stream()
                .map(doctor -> new DoctorDTO(
                        doctor.getIdDoctor(),
                        doctor.getFirstName(),
                        doctor.getLastName(),
                        doctor.getSpecialization(),
                        doctor.getHospitalName(),
                        doctor.isActive()
                ))
                .toList();
    }
}
