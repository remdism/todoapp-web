package com.example.todoapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    // データベースを操作するRepositoryを用意
    private final TaskRepository repository;

    // コンストラクタでRepositoryをセット（Spring Bootが自動で繋いでくれます）
    public TodoController(TaskRepository repository) {
        this.repository = repository;
    }

    // トップページを表示する処理
    @GetMapping("/")
    public String index(Model model) {
        // ① ダミーデータではなく、DBからタスクを全件取得 (findAll)
        model.addAttribute("tasks", repository.findAll());
        return "index";
    }

    // タスクを追加する処理（HTMLの form action="/add" から呼ばれます）
    @PostMapping("/add")
    public String addTask(@RequestParam String title) {
        // ② 画面から送られてきた title を使って新しいTaskを作成
        Task newTask = new Task(title, false);
        
        // ③ DBに保存 (save)
        repository.save(newTask);
        
        // ④ 保存が終わったら、トップページ（/）に自動で移動（リダイレクト）させる
        return "redirect:/";
    }
}