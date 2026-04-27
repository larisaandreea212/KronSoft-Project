package com.fmi_unitbv2026.demo.entity;

import com.fmi_unitbv2026.demo.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ai_report")
public class AIReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private int idReport;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Column(name = "ai_score")
    private int aiScore;

    @Column(name = "ai_note")
    private String aiNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date")
    private LocalDate date;

    public AIReport() { }

    public int getIdReport() { return idReport; }
    public void setIdReport(int idReport) { this.idReport = idReport; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public int getAiScore() { return aiScore; }
    public void setAiScore(int aiScore) { this.aiScore = aiScore; }

    public String getAiNote() { return aiNote; }
    public void setAiNote(String aiNote) { this.aiNote = aiNote; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}