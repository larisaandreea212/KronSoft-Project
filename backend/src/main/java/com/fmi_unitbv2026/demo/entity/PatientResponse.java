package com.fmi_unitbv2026.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_response")
public class PatientResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_response")
    private int idResponse;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_question")
    private Questionnaire question;

    @Column(name = "answer_text")
    private String answerText;

    public PatientResponse() { }

    public int getIdResponse() { return idResponse; }
    public void setIdResponse(int idResponse) { this.idResponse = idResponse; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Questionnaire getQuestion() { return question; }
    public void setQuestion(Questionnaire question) { this.question = question; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
