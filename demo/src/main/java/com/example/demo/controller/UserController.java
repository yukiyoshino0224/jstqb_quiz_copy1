package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ユーザー登録画面を表示
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // resources/templates/register.html を表示
    }

    // フォーム送信処理（登録処理）
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 本来はハッシュ化すべきですが今はプレーンでOK
        userRepository.save(user);
        return "success"; // 登録成功時に表示する resources/templates/success.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // ログイン成功 → 仮のマイページへ遷移
            session.setAttribute("username", username); 
            return "redirect:/menu"; 
        } else {
            // ログイン失敗
            model.addAttribute("error", "ユーザー名またはパスワードが間違っています");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを破棄
        return "redirect:/login"; // ログイン画面にリダイレクト
    }

    @GetMapping("/welcome")
    public String showWelcomePage(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // ログインしてなければログイン画面へ
        }
        return "welcome"; // ログイン済みなら welcome.html を表示
    }

}
