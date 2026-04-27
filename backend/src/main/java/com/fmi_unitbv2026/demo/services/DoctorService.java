package com.fmi_unitbv2026.demo.services;
import com.fmi_unitbv2026.demo.dto.DoctorDTO;
import com.fmi_unitbv2026.demo.entity.Doctor;
import com.fmi_unitbv2026.demo.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorDTO getDoctorById(int id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return new DoctorDTO(
                doctor.getIdDoctor(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getHospitalName()
        );
    }
}
