package dto;

import java.util.List;

public class PatientSummaryDTO {
    private int aiScore;
    private String aiNote;
    private Enum status;
    private List<QuestionResponseDTO> questions;


    public PatientSummaryDTO(int aiScore, List<QuestionResponseDTO> questionResponses, Enum status, String aiNote) {
        this.aiScore = aiScore;
        this.questions = questionResponses;
        this.status = status;
        this.aiNote = aiNote;
    }


    public int getAiScore() {
        return aiScore;
    }
    public void setAiScore(int aiScore) {
        this.aiScore = aiScore;
    }

    public String getAiNote() {
        return aiNote;
    }
    public void setAiNote(String aiNote) {
        this.aiNote = aiNote;
    }

    public Enum getStatus() {
        return status;
    }
    public void setStatus(Enum status) {
        this.status = status;
    }

    public List<QuestionResponseDTO> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionResponseDTO> questions) {
        this.questions = questions;
    }
}
