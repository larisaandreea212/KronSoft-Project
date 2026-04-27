package com.fmi_unitbv2026.demo.dto;

import com.fmi_unitbv2026.demo.enums.ResponseType;

public class QuestionResponseDTO {
    private String questionText;
    private String answerText;
    private ResponseType responseType;

    public QuestionResponseDTO(String questionText, String answerText, ResponseType responseType) {
        this.questionText = questionText;
        this.answerText = answerText;
        this.responseType = responseType;
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

    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
