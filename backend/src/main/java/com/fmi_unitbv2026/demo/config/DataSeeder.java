package com.fmi_unitbv2026.demo.config;

import com.fmi_unitbv2026.demo.entity.*;
import com.fmi_unitbv2026.demo.enums.ResponseType;
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
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            QuestionnaireRepository questionnaireRepository,
            PatientResponseRepository patientResponseRepository,
            AiReportRepository aiReportRepository
    ) {
        return args -> {

            if (doctorRepository.count() > 0) {
                return;
            }

            Doctor doctor1 = new Doctor();
            doctor1.setFirstName("Andrei");
            doctor1.setLastName("Popescu");
            doctor1.setSpecialization("Chirurgie generala");
            doctor1.setHospitalName("Spitalul Clinic Brasov");

            Doctor doctor2 = new Doctor();
            doctor2.setFirstName("Maria");
            doctor2.setLastName("Ionescu");
            doctor2.setSpecialization("Cardiologie");
            doctor2.setHospitalName("Spitalul Judetean Brasov");

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);

            Patient p1 = new Patient();
            p1.setFirstName("Ion");
            p1.setLastName("Dumitrescu");
            p1.setAge(54);
            p1.setSex("M");
            p1.setCNP("1700101123456");
            p1.setSurgeryType("Apendicectomie");
            p1.setSurgeryDate(LocalDate.now().minusDays(5));
            p1.setDoctor(doctor1);

            Patient p2 = new Patient();
            p2.setFirstName("Elena");
            p2.setLastName("Marin");
            p2.setAge(62);
            p2.setSex("F");
            p2.setCNP("2640202123456");
            p2.setSurgeryType("Colecistectomie");
            p2.setSurgeryDate(LocalDate.now().minusDays(3));
            p2.setDoctor(doctor1);

            Patient p3 = new Patient();
            p3.setFirstName("Mihai");
            p3.setLastName("Stan");
            p3.setAge(47);
            p3.setSex("M");
            p3.setCNP("1770303123456");
            p3.setSurgeryType("Interventie cardiaca");
            p3.setSurgeryDate(LocalDate.now().minusDays(7));
            p3.setDoctor(doctor2);

            patientRepository.save(p1);
            patientRepository.save(p2);
            patientRepository.save(p3);

            Questionnaire q1 = new Questionnaire();
            q1.setQuestionText("Do you have a fever?");
            q1.setResponseType(ResponseType.YES_NO);
            q1.setWeight(20);
            q1.setInverted(false);

            Questionnaire q2 = new Questionnaire();
            q2.setQuestionText("Pain level (1-5)");
            q2.setResponseType(ResponseType.SCALE_1_5);
            q2.setWeight(30);
            q2.setInverted(false);

            Questionnaire q3 = new Questionnaire();
            q3.setQuestionText("Fatigue level (1-5)");
            q3.setResponseType(ResponseType.SCALE_1_5);
            q3.setWeight(25);
            q3.setInverted(false);

            Questionnaire q4 = new Questionnaire();
            q4.setQuestionText("Do you feel better than yesterday?");
            q4.setResponseType(ResponseType.YES_NO);
            q4.setWeight(25);
            q4.setInverted(true);

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