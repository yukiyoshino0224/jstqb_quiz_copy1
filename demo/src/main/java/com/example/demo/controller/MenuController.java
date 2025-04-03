package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.service.QuizService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    private final QuizService quizService;

    public MenuController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/evaluate")
    public String evaluateAnswers(Model model) {
        // 仮に選択した回答リスト
        List<Integer> selectedAnswers = List.of(1, 2, 3);  // 実際にはフォームから取得する

        
        Result result = quizService.evaluateAnswers(selectedAnswers);

        model.addAttribute("result", result);
        return "result"; 
}
}