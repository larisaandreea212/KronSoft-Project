package com.fmi_unitbv2026.demo.dto;

import com.fmi_unitbv2026.demo.enums.ResponseType;

public class QuestionsDTO {
    private int idQuestions;
    private String questionText;
    private ResponseType responseType;


    public QuestionsDTO(int idQuestions, ResponseType responseType, String questionText) {
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

    public ResponseType getResponseType() {
        return responseType;
    }
    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}
