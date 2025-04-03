package com.example.demo.service;

import com.example.demo.model.Result;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {
    public Result evaluateAnswers(List<Integer> selectedAnswers) {
        int correctCount = 0;

        // 仮の正解判定
        for (int answer : selectedAnswers) {
            if (answer == 1) { // 仮の正解処理
                correctCount++;
            }
        }

        // 結果を `Result` 型で返す
        Result result = new Result();
        result.setCorrectCount(correctCount);
        result.setMessage("You have " + correctCount + " correct answers.");

        return result;
    }
}
