package com.example.demo.model;

public class Result {
    private int correctCount;
    private int totalCount;
    private String message;

    // ★ デフォルトコンストラクタ
    public Result() {
        this.correctCount = 0;
        this.totalCount = 0;
        this.message = "";
    }

    // ★ 引数ありコンストラクタ
    public Result(int correctCount, int totalCount) {
        this.correctCount = correctCount;
        this.totalCount = totalCount;
        this.message = "あなたのスコアは " + correctCount + " / " + totalCount + " です！";
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
