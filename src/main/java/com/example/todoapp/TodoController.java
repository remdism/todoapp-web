package com.example.todoapp;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    private final TaskRepository repository;
    private final CalendarService calendarService;
    public TodoController(TaskRepository repository, CalendarService calendarService) {
        this.repository = repository;
        this.calendarService = calendarService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Task> tasks = repository.findAll();
        model.addAttribute("tasks", tasks);
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        List<Integer> calendarDays = calendarService.getCalendarDays(year, month);

        // 今月のタスクのうち、期日が設定されている「日付」を抽出して青丸をつける
        Map<Integer, Boolean> hasTaskMap = tasks.stream()
            .filter(t -> t.getDueDate() != null && t.getDueDate().getYear() == year && t.getDueDate().getMonthValue() == month)
            .collect(Collectors.toMap(
                t -> t.getDueDate().getDayOfMonth(),
                t -> true,
                (existing, replacement) -> true // 同じ日に複数タスクがあってもOK
            ));

        // 画面へ送信
        model.addAttribute("currentYear", year);
        model.addAttribute("currentMonth", month);
        model.addAttribute("todayDay", today.getDayOfMonth());
        model.addAttribute("calendarDays", calendarDays);
        model.addAttribute("hasTaskMap", hasTaskMap);

        return "index";
    }
    
    // タスクを追加する処理
    @PostMapping("/add")
    public String addTask(@RequestParam String title, @RequestParam LocalDate dueDate) {
        Task newTask = new Task(title, false, dueDate);
        repository.save(newTask);
        return "redirect:/";
    }
    
    // タスクを完了にする処理
    @PostMapping("/complete")
    public String completeTask(@RequestParam int id) {
        // 送られてきたIDをもとに、データベースから対象のタスクを探す
        Task task = repository.findById(id).orElse(null);
        
        if (task != null) {
            // 状態を「完了(true)」に変更して、データベースに上書き保存
            task.setCompleted(true);
            repository.save(task);
        }
        return "redirect:/";
    }

    // タスクを削除する処理
    @PostMapping("/delete")
    public String deleteTask(@RequestParam int id) {
        // 送られてきたIDのタスクをデータベースから直接削除
        repository.deleteById(id);
        
        return "redirect:/";
    }
}