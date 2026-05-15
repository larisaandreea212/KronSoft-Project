package com.fmi_unitbv2026.demo.dto;

public class AnswerDTO {
    private int idQuestion;
    private String answerText;

    public AnswerDTO(int idQuestion, String answerText){
        this.idQuestion = idQuestion;
        this.answerText = answerText;
    }
    public int getIdQuestion() {return idQuestion;}
    public void setIdQuestion(int idQuestion) {this.idQuestion = idQuestion;}

    public String getAnswerText() {return answerText;}
    public void setAnswerText(String answerText) {this.answerText = answerText;}
}
