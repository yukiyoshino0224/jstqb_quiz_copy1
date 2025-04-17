package com.example.demo.model;

import java.util.List;

public class QuestionView {
    private String question;
    private boolean isCorrect;
    private List<Choice> choices;
    private Long selectedChoiceId;

    // Getter / Setter
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
    
    public Long getSelectedChoiceId() {
        return selectedChoiceId;
    }

    public void setSelectedChoiceId(Long selectedChoiceId) {
        this.selectedChoiceId = selectedChoiceId;
    }
}