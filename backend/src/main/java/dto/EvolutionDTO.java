package dto;

import java.time.LocalDate;

public class EvolutionDTO {
    private LocalDate date;
    private int aiScore;

    public EvolutionDTO(LocalDate date, int aiScore) {
        this.date = date;
        this.aiScore = aiScore;
    }


    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAiScore() {
        return aiScore;
    }
    public void setAiScore(int aiScore) {
        this.aiScore = aiScore;
    }
}
