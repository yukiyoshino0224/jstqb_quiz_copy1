package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.model.Answer;
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
    private final AnswerRepository answerRepository;

    public MenuController(QuizService quizService, AnswerRepository answerRepository) {
    this.quizService = quizService;
    this.answerRepository = answerRepository;
}

   /*  @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    } */

    @GetMapping("/evaluate")
    public String evaluateAnswers(Model model) {
    List<Answer> answers = answerRepository.findAll(); // ←全件とって評価！
    int correctCount = (int) answers.stream().filter(Answer::isCorrect).count(); // 正解の数カウント

    Result result = new Result(correctCount, answers.size()); // ←Result に詰める（作ってる？）
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

    @PostMapping("/submit")
@ResponseBody
public String handleAnswer(@RequestParam("answer") Long selectedChoiceId, HttpSession session) {
    System.out.println("受け取った answerId: " + selectedChoiceId);

    // 現在の質問リストとインデックスをセッションから取得
    List<Question> questions = (List<Question>) session.getAttribute("mockExamQuestions");
    Integer currentQuestionIndex = (Integer) session.getAttribute("currentQuestionIndex");

    if (questions == null || currentQuestionIndex == null || currentQuestionIndex >= questions.size()) {
        return "セッションが見つからないか、問題番号が不正です";
    }

    Question currentQuestion = questions.get(currentQuestionIndex);

    // 選択肢が正解かチェック
    boolean isCorrect = currentQuestion.getChoices().stream()
        .filter(Choice::isCorrect)
        .anyMatch(choice -> choice.getId().equals(selectedChoiceId));

    // 回答を保存
    Answer answer = new Answer();
    answer.setQuestionId(currentQuestion.getId());
    answer.setSelectedChoiceId(selectedChoiceId);
    answer.setCorrect(isCorrect);

    // ここでAnswerRepository使って保存！
    answerRepository.save(answer);

    // 次の問題用にインデックスを更新（必要なら）
    session.setAttribute("currentQuestionIndex", currentQuestionIndex + 1);

    return "OK";
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
    @GetMapping("/menu")
    public String showMenuPage(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 未ログインの場合はログインページへリダイレクト
            return "redirect:/login";
        }

        // ログイン済みならmenu.htmlを表示
        return "menu";
    }
}
