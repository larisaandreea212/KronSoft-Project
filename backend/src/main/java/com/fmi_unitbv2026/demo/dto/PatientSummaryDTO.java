package com.fmi_unitbv2026.demo.dto;

import com.fmi_unitbv2026.demo.enums.Status;

import java.util.List;

public class PatientSummaryDTO {
    private int aiScore;
    private String aiNote;
    private Status status;
    private List<QuestionResponseDTO> questions;


    public PatientSummaryDTO(int aiScore, List<QuestionResponseDTO> questionResponses, Status status, String aiNote) {
        this.aiScore = aiScore;
        this.questions = questionResponses;
        this.status = status;
        this.aiNote = aiNote;
    }


    public int getAiScore() {
        return aiScore;
    }
    public void setAiScore(int aiScore) {
        this.aiScore = aiScore;
    }

    public String getAiNote() {
        return aiNote;
    }
    public void setAiNote(String aiNote) {
        this.aiNote = aiNote;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public List<QuestionResponseDTO> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionResponseDTO> questions) {
        this.questions = questions;
    }


}
