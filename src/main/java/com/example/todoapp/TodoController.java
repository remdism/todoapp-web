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
    
    // タスクを完了にする処理
    @PostMapping("/complete")
    public String completeTask(@RequestParam int id) {
        // ① 送られてきたIDをもとに、データベースから対象のタスクを探す
        Task task = repository.findById(id).orElse(null);
        
        if (task != null) {
            // ② 状態を「完了(true)」に変更して、データベースに上書き保存
            task.setCompleted(true);
            repository.save(task);
        }
        return "redirect:/";
    }

    // タスクを削除する処理
    @PostMapping("/delete")
    public String deleteTask(@RequestParam int id) {
        // ① 送られてきたIDのタスクをデータベースから直接削除
        repository.deleteById(id);
        
        return "redirect:/";
    }
}