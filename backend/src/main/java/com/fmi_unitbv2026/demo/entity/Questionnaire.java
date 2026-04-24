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
}