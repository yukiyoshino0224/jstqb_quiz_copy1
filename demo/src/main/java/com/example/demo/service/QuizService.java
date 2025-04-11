package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.Result;
import com.example.demo.model.Choice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.springframework.transaction.annotation.Transactional;

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
    // getQuestionsByChapterメソッド
    public List<Question> getQuestionsByChapter(int chapterNumber) {
        return questionRepository.findByChapter(chapterNumber);  // Repositoryを使ってデータベースから質問を取得
    }

    // 模擬試験用：1〜6章の問題からランダムに40問を取得
    @Transactional
    public List<Question> getRandomQuestionsForMockExam() {
        List<Question> allQuestions = new ArrayList<>();

        // 1〜6章のすべての問題を取得
        for (int chapter = 1; chapter <= 6; chapter++) {
            allQuestions.addAll(getQuestionsByChapter(chapter));
        }

        // choices を強制的に読み込む（Hibernateセッション内で）
        for (Question q : allQuestions) {
            q.getChoices().size();  // これでLAZYロードされる
        }

        // ランダムにシャッフル
        Collections.shuffle(allQuestions);

        // 最初の40問を選択
        return allQuestions.subList(0, 40);
    }    
}
