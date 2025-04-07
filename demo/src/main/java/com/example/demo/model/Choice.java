package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "choices")  // choices テーブルを使用
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // 問題との関連を追加

    private String choiceText; // 選択肢のテキスト

    private boolean isCorrect;  // 正しい選択肢かどうかを示すフィールド

    // Getter と Setter メソッド
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
