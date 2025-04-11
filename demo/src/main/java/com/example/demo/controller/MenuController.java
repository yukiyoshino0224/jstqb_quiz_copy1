package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.model.Choice;
import com.example.demo.model.Question;
import com.example.demo.service.QuizService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

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

    // 模擬試験の初期画面（ランダムな40問）
    @GetMapping("/quiz/random")
    @Transactional
    public String startMockExam(Model model, HttpSession session) {
        List<Question> mockExamQuestions = quizService.getRandomQuestionsForMockExam();

        // 最初の問題を表示
        if (!mockExamQuestions.isEmpty()) {
            Question question = mockExamQuestions.get(0);

            session.setAttribute("mockExamQuestions", mockExamQuestions);

             // 正解の選択肢
            Choice correctChoice = question.getChoices().stream()
            .filter(Choice::isCorrect)
            .findFirst()
            .orElse(null);

            // 問題の情報を設定
            model.addAttribute("correctChoiceText", correctChoice != null ? correctChoice.getChoiceText() : "正解なし"); // ここでは仮に"正解なし"
            model.addAttribute("question", question);
            model.addAttribute("hasNext", mockExamQuestions.size() > 1); // 次の問題があるかの判定
            model.addAttribute("displayNumber", 1);
        }

        return "quiz";
    }

    // 模擬試験の次の問題
    @GetMapping("/quiz/random/question/{questionNumber}")
    public String showMockExamQuestion(
        @PathVariable int questionNumber,
        Model model,
        HttpSession session
    ) {
        List<Question> mockExamQuestions = (List<Question>) session.getAttribute("mockExamQuestions");

        // もし範囲外の番号ならエラーページ
        if (mockExamQuestions == null || questionNumber < 1 || questionNumber > mockExamQuestions.size()) {
            System.out.println("ERROR: mockExamQuestions is null in model");
            model.addAttribute("message", "問題が存在しません");
            return "error";
        }

        // 現在の問題を取得
        Question question = mockExamQuestions.get(questionNumber - 1);
    
        // 正解の選択肢
        Choice correctChoice = question.getChoices().stream()
            .filter(Choice::isCorrect)
            .findFirst()
            .orElse(null);

    model.addAttribute("correctChoiceText", correctChoice != null ? correctChoice.getChoiceText() : "正解なし");
    model.addAttribute("question", question);
    model.addAttribute("hasNext", questionNumber < mockExamQuestions.size());
    model.addAttribute("displayNumber", questionNumber);

        return "quiz";
    }
}
