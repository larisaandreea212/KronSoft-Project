package dto;

public class QuestionResponseDTO {
    private String questionText;
    private String answerText;
    private Enum responseType;

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

    public Enum getResponseType() {
        return responseType;
    }
    public void setResponseType(Enum responseType) {
        this.responseType = responseType;
    }

    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
