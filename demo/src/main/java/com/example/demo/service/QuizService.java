package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.QuestionRepository;
import java.util.List;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;

    @Autowired
public QuizService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
}

    // 仮の正解判定
    public Result evaluateAnswers(List<Integer> selectedAnswers) {
        int correctCount = 0;

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

    // 新たに追加したgetQuestionByChapterメソッド
    public Question getQuestionByChapter(int chapterNumber) {
        // ここでは仮のデータを返します。実際にはデータベースから取得する必要があります。
        Question question = new Question();
        question.setChapter(chapterNumber);
        question.setQuestion("第" + chapterNumber + "章の問題の例");

        // 仮の選択肢を設定
        // ここでは仮に選択肢をセットしていますが、実際にはデータベースから取得することになるでしょう。
        List<String> choices = List.of("選択肢1", "選択肢2", "選択肢3", "選択肢4");
        question.setChoices(choices);

        return question;
    }

    // getQuestionsByChapterメソッドを追加
    public List<Question> getQuestionsByChapter(int chapterNumber) {
        // 章番号に基づいてデータベースから質問を取得
        return questionRepository.findByChapter(chapterNumber);  // Repositoryを使ってデータベースから質問を取得
    }

}
