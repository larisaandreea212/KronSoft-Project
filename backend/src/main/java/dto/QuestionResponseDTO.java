package dto;

import com.fmi_unitbv2026.demo.enums.Status;

public class QuestionResponseDTO {
    private String questionText;
    private String answerText;
    private Status responseType;

    public QuestionResponseDTO(String questionText, String answerText, Enum responseType) {
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

    public Status getResponseType() {
        return responseType;
    }
    public void setResponseType(Status responseType) {
        this.responseType = responseType;
    }

    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
