package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.model.Choice;
import com.example.demo.model.Question;
import com.example.demo.service.QuizService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MenuController {

    private final QuizService quizService;

    public MenuController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    @GetMapping("/evaluate")
    public String evaluateAnswers(Model model) {
        List<Integer> selectedAnswers = List.of(1, 2, 3); // 仮データ
        Result result = quizService.evaluateAnswers(selectedAnswers);
        model.addAttribute("result", result);
        return "result";
    }

    // クイズページ表示（指定された章と問題番号）
    @GetMapping("/chapter/{chapterNumber}/question/{questionNumber}")
    public String showQuestionByNumber(
        @PathVariable int chapterNumber,
        @PathVariable int questionNumber,
        Model model
    ) {
        List<Question> questions = quizService.getQuestionsByChapter(chapterNumber);

        // 問題番号が範囲内かチェック
        if (!questions.isEmpty() && questionNumber >= 1 && questionNumber <= questions.size()) {
            Question question = questions.get(questionNumber - 1);

            // 正解の選択肢を取得
            Choice correctChoice = question.getChoices().stream()
                .filter(Choice::isCorrect)
                .findFirst()
                .orElse(null);

            model.addAttribute("correctChoiceText", correctChoice != null ? correctChoice.getChoiceText() : "正解なし");
            model.addAttribute("chapterNumber", chapterNumber);
            model.addAttribute("chapterTitle", question.getChapterTitle());
            model.addAttribute("displayNumber", questionNumber);
            model.addAttribute("question", question);
            model.addAttribute("hasNext", questionNumber < questions.size());
        } else {
            // 範囲外だった場合
            model.addAttribute("chapterNumber", chapterNumber);
            model.addAttribute("chapterTitle", "該当なし");
            model.addAttribute("displayNumber", questionNumber);
            model.addAttribute("question", null);
            model.addAttribute("correctChoiceText", "問題が存在しません");
            model.addAttribute("hasNext", false);
        }

        return "quiz";
    }

    // 最初の問題（デフォルト表示）
    @GetMapping("/chapter/{chapterNumber}")
    public String showChapter(@PathVariable int chapterNumber, Model model) {
        return "redirect:/chapter/" + chapterNumber + "/question/1";
    }
}
