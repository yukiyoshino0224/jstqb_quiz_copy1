package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.service.QuizService;
import com.example.demo.model.Choice;
import com.example.demo.model.Question;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MenuController {
    private final QuizService quizService;

    public MenuController(QuizService quizService) {
        this.quizService = quizService;
    }

    // メニュー表示
    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    // 回答を評価する
    @GetMapping("/evaluate")
    public String evaluateAnswers(Model model) {

        // 仮に選択した回答リスト
        List<Integer> selectedAnswers = List.of(1, 2, 3);  // 実際にはフォームから取得する
        Result result = quizService.evaluateAnswers(selectedAnswers);
        model.addAttribute("result", result);
        return "result"; 
    }

    @GetMapping("/chapter/{chapterNumber}")
    public String showChapter(@PathVariable int chapterNumber, Model model) {
    // 複数問題取得
    List<Question> questions = quizService.getQuestionsByChapter(chapterNumber);

    // もしquestionsリストが空でなければ、displayNumber番目の問題を渡す（空だったら渡さない）
    if (!questions.isEmpty()) {
        int displayNumber = 1;  // 仮の問題番号を設定
        Question question = questions.get(displayNumber - 1);  // displayNumber番目の問題を取得

         // 正解の選択肢を取得
         Choice correctChoice = question.getChoices().stream()
    .filter(Choice::isCorrect) // 正解の選択肢を取得
    .findFirst() 
    .orElse(null); // 見つからなければnullを設定

// 正解があればその選択肢をmodelに追加
if (correctChoice != null) {
    model.addAttribute("correctChoiceText", correctChoice.getChoiceText()); 
} else {
    model.addAttribute("correctChoiceText", "正解なし");
}

        model.addAttribute("chapterNumber", chapterNumber);
        model.addAttribute("chapterTitle", question.getChapterTitle());
        model.addAttribute("displayNumber", displayNumber);
        model.addAttribute("question", question);
    }

    return "quiz";
}

}
