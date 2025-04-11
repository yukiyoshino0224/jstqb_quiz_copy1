package com.example.demo.repository;

import com.example.demo.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // chapterNumberに基づいてQuestionを取得するクエリメソッドを定義
    List<Question> findByChapter(int chapter);
}