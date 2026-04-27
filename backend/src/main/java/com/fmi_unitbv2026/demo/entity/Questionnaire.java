package com.fmi_unitbv2026.demo.entity;

import com.fmi_unitbv2026.demo.enums.ResponseType;
import jakarta.persistence.*;

@Entity
@Table(name = "questionnaire")
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_question")
    private int idQuestion;

    @Column(name = "question_text")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_type")
    private ResponseType responseType;

    @Column(name = "weight")
    private double weight;

    @Column(name = "is_inverted")
    private boolean isInverted;

    public Questionnaire() { }

    public boolean isInverted() { return isInverted; }
    public void setInverted(boolean inverted) { isInverted = inverted; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public ResponseType getResponseType() { return responseType; }
    public void setResponseType(ResponseType responseType) { this.responseType = responseType; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public int getIdQuestion() { return idQuestion; }
    public void setIdQuestion(int idQuestion) { this.idQuestion = idQuestion; }
}