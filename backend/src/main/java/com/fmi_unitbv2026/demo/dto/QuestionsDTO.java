package com.fmi_unitbv2026.demo.dto;

public class QuestionsDTO {
    private int idQuestions;
    private String questionText;
    private String responseType;


    public QuestionsDTO(int idQuestions, String responseType, String questionText) {
        this.idQuestions = idQuestions;
        this.responseType = responseType;
        this.questionText = questionText;
    }

    public int getIdQuestions() {
        return idQuestions;
    }
    public void setIdQuestions(int idQuestions) {
        this.idQuestions = idQuestions;
    }

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getResponseType() {
        return responseType;
    }
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
