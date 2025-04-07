package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.Result;
import com.example.demo.model.Choice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.QuestionRepository;
import java.util.ArrayList;
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
        // 仮のデータを返します。実際にはデータベースから取得する必要があります。
        Question question = new Question();
        question.setChapter(chapterNumber);
        question.setQuestion("第" + chapterNumber + "章の問題の例");

        // 仮の選択肢を設定
        List<String> choices = List.of("選択肢1", "選択肢2", "選択肢3", "選択肢4");
        
        // List<String> から List<Choice> に変換
        List<Choice> choiceList = new ArrayList<>();
        for (int i = 0; i < choices.size(); i++) {
            Choice choice = new Choice();
            choice.setChoiceText(choices.get(i));
            choice.setCorrect(i == 0); // 仮に最初の選択肢を正解に設定
            choice.setQuestion(question); // どの問題に属するか設定
            choiceList.add(choice);
        }

        // Choiceのリストをセット
        question.setChoices(choiceList);

        return question;
    }

    // getQuestionsByChapterメソッドを追加
    public List<Question> getQuestionsByChapter(int chapterNumber) {
        // 章番号に基づいてデータベースから質問を取得
        return questionRepository.findByChapter(chapterNumber);  // Repositoryを使ってデータベースから質問を取得
    }
}
