package com.fmi_unitbv2026.demo.dto;

import java.util.List;

public class QuestionnaireSubmissionDTO {
    private int idPatient;
    private List<AnswerDTO> answers;


    public QuestionnaireSubmissionDTO(int idPatient, List<AnswerDTO> answers) {
        this.idPatient = idPatient;
        this.answers = answers;
    }

    public int getIdPatient() {
        return idPatient;
    }
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }
    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
