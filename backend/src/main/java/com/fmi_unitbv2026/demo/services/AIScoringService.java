package com.fmi_unitbv2026.demo.services;

import com.fmi_unitbv2026.demo.entity.PatientResponse;
import com.fmi_unitbv2026.demo.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIScoringService {

    private static final Logger logger = LoggerFactory.getLogger(AIScoringService.class);

    public int calculateScore(List<PatientResponse> responses) {

        if (responses == null || responses.isEmpty()) {
            logger.warn("calculateScore called with null or empty responses list.");
            return 0;
        }

        double totalScore = 0;
        double maxScore = 0;

        for (PatientResponse response : responses) {

            double weight = response.getQuestion().getWeight();
            boolean inverted = response.getQuestion().isInverted();

            double value;
            try {
                value = mapAnswerToValue(response.getAnswerText());
            } catch (IllegalArgumentException e) {
                logger.error("Unknown answer '{}' for question '{}'. Defaulting to 0.",
                        response.getAnswerText(),
                        response.getQuestion().getQuestionText());
                value = 0;
            }

            if (inverted) {
                value = 1 - value;
            }

            totalScore += value * weight;
            maxScore += weight;
        }

        if (maxScore == 0) {
            return 0;
        }

        return (int) Math.round((totalScore / maxScore) * 100);
    }

    private double mapAnswerToValue(String answer) {

        if (answer == null) {
            return 0;
        }

        String normalizedAnswer = answer.toLowerCase().trim();

        switch (normalizedAnswer) {
            case "yes":
            case "da":
            case "true":
                return 1.0;

            case "no":
            case "nu":
            case "false":
                return 0.0;

            case "low":
            case "mic":
                return 0.3;

            case "medium":
            case "mediu":
            case "moderate":
                return 0.6;

            case "high":
            case "mare":
            case "severe":
                return 1.0;

            default:
                throw new IllegalArgumentException("Unknown answer value: " + answer);
        }
    }

    public Status determineStatus(int score) {

        if (score >= 70) {
            return Status.CRITICAL;
        }

        if (score >= 40) {
            return Status.MODERATE;
        }

        return Status.STABLE;
    }

    public String generateNote(List<PatientResponse> responses, int score, Status status) {

        if (responses == null || responses.isEmpty()) {
            return "No patient responses available for analysis.";
        }

        StringBuilder note = new StringBuilder();

        note.append("Patient analysis: ");

        switch (status) {
            case CRITICAL:
                note.append("high risk condition detected. ");
                break;

            case MODERATE:
                note.append("moderate risk condition detected. ");
                break;

            case STABLE:
                note.append("patient is currently stable. ");
                break;
        }

        note.append("Key observations: ");

        boolean hasConcern = false;

        for (PatientResponse response : responses) {

            String question = response.getQuestion().getQuestionText();
            String answer = response.getAnswerText();

            if (isCriticalAnswer(answer)) {
                note.append("[")
                        .append(question)
                        .append("] indicates HIGH concern. ");
                hasConcern = true;

            } else if (isModerateAnswer(answer)) {
                note.append("[")
                        .append(question)
                        .append("] indicates MODERATE concern. ");
                hasConcern = true;
            }
        }

        if (!hasConcern) {
            note.append("No major concerning answers were detected. ");
        }

        note.append("Overall AI score: ")
                .append(score)
                .append("/100. ");

        if (status == Status.CRITICAL) {
            if (score >= 90) {
                note.append("URGENT: Immediate medical attention is strongly required.");
            } else {
                note.append("Immediate medical attention is recommended.");
            }
        } else if (status == Status.MODERATE) {
            note.append("Monitoring and further evaluation are advised.");
        } else {
            note.append("No immediate concerns detected.");
        }

        return note.toString();
    }

    private boolean isCriticalAnswer(String answer) {

        if (answer == null) {
            return false;
        }

        String normalizedAnswer = answer.toLowerCase().trim();

        return normalizedAnswer.equals("yes")
                || normalizedAnswer.equals("da")
                || normalizedAnswer.equals("high")
                || normalizedAnswer.equals("mare")
                || normalizedAnswer.equals("severe");
    }

    private boolean isModerateAnswer(String answer) {

        if (answer == null) {
            return false;
        }

        String normalizedAnswer = answer.toLowerCase().trim();

        return normalizedAnswer.equals("moderate")
                || normalizedAnswer.equals("mediu")
                || normalizedAnswer.equals("medium");
    }
}