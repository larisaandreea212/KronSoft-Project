package com.fmi_unitbv2026.demo.config;

import com.fmi_unitbv2026.demo.entity.*;
import com.fmi_unitbv2026.demo.enums.ResponseType;
import com.fmi_unitbv2026.demo.enums.Role;
import com.fmi_unitbv2026.demo.enums.Status;
import com.fmi_unitbv2026.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            ReceptionistRepository receptionistRepository,
            QuestionnaireRepository questionnaireRepository,
            PatientResponseRepository patientResponseRepository,
            AiReportRepository aiReportRepository
    ) {
        return args -> {

            if (userRepository.count() > 0) {
                return;
            }

            User doctorUser1 = new User("andrei.popescu@hospital.com", "firebase-doctor-1", Role.DOCTOR);
            User doctorUser2 = new User("maria.ionescu@hospital.com", "firebase-doctor-2", Role.DOCTOR);
            User patientUser1 = new User("ion.dumitrescu@email.com", "firebase-patient-1", Role.PATIENT);
            User patientUser2 = new User("elena.marin@email.com", "firebase-patient-2", Role.PATIENT);
            User patientUser3 = new User("mihai.stan@email.com", "firebase-patient-3", Role.PATIENT);
            User receptionistUser = new User("reception@hospital.com", "firebase-receptionist-1", Role.RECEPTIONIST);

            userRepository.save(doctorUser1);
            userRepository.save(doctorUser2);
            userRepository.save(patientUser1);
            userRepository.save(patientUser2);
            userRepository.save(patientUser3);
            userRepository.save(receptionistUser);

            Doctor doctor1 = new Doctor();
            doctor1.setUser(doctorUser1);
            doctor1.setFirstName("Andrei");
            doctor1.setLastName("Popescu");
            doctor1.setSpecialization("General Surgery");
            doctor1.setHospitalName("Brasov Clinical Hospital");
            doctor1.setActive(true);

            Doctor doctor2 = new Doctor();
            doctor2.setUser(doctorUser2);
            doctor2.setFirstName("Maria");
            doctor2.setLastName("Ionescu");
            doctor2.setSpecialization("Cardiology");
            doctor2.setHospitalName("Brasov County Hospital");
            doctor2.setActive(true);

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);

            Receptionist receptionist = new Receptionist();
            receptionist.setUser(receptionistUser);
            receptionist.setFirstName("Ana");
            receptionist.setLastName("Georgescu");
            receptionistRepository.save(receptionist);

            Patient p1 = new Patient();
            p1.setUser(patientUser1);
            p1.setFirstName("Ion");
            p1.setLastName("Dumitrescu");
            p1.setAge(54);
            p1.setSex("M");
            p1.setCNP("1700101123456");
            p1.setSurgeryType("Appendectomy");
            p1.setSurgeryDate(LocalDate.now().minusDays(5));
            p1.setDoctor(doctor1);

            Patient p2 = new Patient();
            p2.setUser(patientUser2);
            p2.setFirstName("Elena");
            p2.setLastName("Marin");
            p2.setAge(62);
            p2.setSex("F");
            p2.setCNP("2640202123456");
            p2.setSurgeryType("Cholecystectomy");
            p2.setSurgeryDate(LocalDate.now().minusDays(3));
            p2.setDoctor(doctor1);

            Patient p3 = new Patient();
            p3.setUser(patientUser3);
            p3.setFirstName("Mihai");
            p3.setLastName("Stan");
            p3.setAge(47);
            p3.setSex("M");
            p3.setCNP("1770303123456");
            p3.setSurgeryType("Cardiac intervention");
            p3.setSurgeryDate(LocalDate.now().minusDays(7));
            p3.setDoctor(doctor2);

            patientRepository.save(p1);
            patientRepository.save(p2);
            patientRepository.save(p3);

            Questionnaire q1 = createQuestion("Do you have a fever?", ResponseType.YES_NO, 20, false);
            Questionnaire q2 = createQuestion("Pain level (1-5)", ResponseType.SCALE_1_5, 30, false);
            Questionnaire q3 = createQuestion("Fatigue level (1-5)", ResponseType.SCALE_1_5, 25, false);
            Questionnaire q4 = createQuestion("Do you feel better than yesterday?", ResponseType.YES_NO, 25, true);

            questionnaireRepository.save(q1);
            questionnaireRepository.save(q2);
            questionnaireRepository.save(q3);
            questionnaireRepository.save(q4);

            saveResponse(patientResponseRepository, p1, q1, "No");
            saveResponse(patientResponseRepository, p1, q2, "2");
            saveResponse(patientResponseRepository, p1, q3, "3");
            saveResponse(patientResponseRepository, p1, q4, "Yes");

            saveResponse(patientResponseRepository, p2, q1, "Yes");
            saveResponse(patientResponseRepository, p2, q2, "5");
            saveResponse(patientResponseRepository, p2, q3, "4");
            saveResponse(patientResponseRepository, p2, q4, "No");

            saveResponse(patientResponseRepository, p3, q1, "No");
            saveResponse(patientResponseRepository, p3, q2, "3");
            saveResponse(patientResponseRepository, p3, q3, "3");
            saveResponse(patientResponseRepository, p3, q4, "No");

            saveReport(aiReportRepository, p1, 28, Status.STABLE,
                    "The patient is stable. Regular monitoring is recommended.",
                    LocalDate.now().minusDays(2));

            saveReport(aiReportRepository, p1, 35, Status.STABLE,
                    "Good evolution, no significant signs of deterioration.",
                    LocalDate.now().minusDays(1));

            saveReport(aiReportRepository, p2, 86, Status.CRITICAL,
                    "The patient presents critical risk. Urgent medical evaluation is recommended.",
                    LocalDate.now());

            saveReport(aiReportRepository, p3, 58, Status.CRITICAL,
                    "The patient shows signs of deterioration. Close monitoring is recommended.",
                    LocalDate.now());
        };
    }

    private Questionnaire createQuestion(String text, ResponseType type, double weight, boolean inverted) {
        Questionnaire q = new Questionnaire();
        q.setQuestionText(text);
        q.setResponseType(type);
        q.setWeight(weight);
        q.setInverted(inverted);
        return q;
    }

    private void saveResponse(PatientResponseRepository repository,
                              Patient patient,
                              Questionnaire question,
                              String answerText) {
        PatientResponse response = new PatientResponse();
        response.setPatient(patient);
        response.setQuestion(question);
        response.setAnswerText(answerText);
        repository.save(response);
    }

    private void saveReport(AiReportRepository repository,
                            Patient patient,
                            int aiScore,
                            Status status,
                            String aiNote,
                            LocalDate date) {
        AIReport report = new AIReport();
        report.setPatient(patient);
        report.setAiScore(aiScore);
        report.setStatus(status);
        report.setAiNote(aiNote);
        report.setDate(date);
        repository.save(report);
    }
}