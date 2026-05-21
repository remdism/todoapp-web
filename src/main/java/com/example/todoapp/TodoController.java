package com.example.todoapp;

import java.time.LocalDate;
import java.time.YearMonth;
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
    // URLから year と month を受け取る
    public String index(@RequestParam(name = "year", required = false) Integer year,
                        @RequestParam(name = "month", required = false) Integer month,
                        Model model) {
        
        List<Task> tasks = repository.findAll();
        model.addAttribute("tasks", tasks);

        LocalDate today = LocalDate.now();
        
        // 指定がなければ「今月」にする
        int targetYear = (year != null) ? year : today.getYear();
        int targetMonth = (month != null) ? month : today.getMonthValue();

        // 前月と翌月の計算
        YearMonth currentYM = YearMonth.of(targetYear, targetMonth);
        YearMonth prevYM = currentYM.minusMonths(1);
        YearMonth nextYM = currentYM.plusMonths(1);

        // 指定された月のカレンダー配列を取得
        List<Integer> calendarDays = calendarService.getCalendarDays(targetYear, targetMonth);

        // タスクの抽出も、表示している月に合わせる
        Map<Integer, Boolean> hasTaskMap = tasks.stream()
            .filter(t -> t.getDueDate() != null && t.getDueDate().getYear() == targetYear && t.getDueDate().getMonthValue() == targetMonth)
            .collect(Collectors.toMap(
                t -> t.getDueDate().getDayOfMonth(),
                t -> true,
                (existing, replacement) -> true
            ));

        // 今日の日付ハイライトは、「表示している月が今月の場合のみ」数字をセットする
        int todayDay = (today.getYear() == targetYear && today.getMonthValue() == targetMonth) ? today.getDayOfMonth() : 0;

        // 画面へデータを送信
        model.addAttribute("currentYear", targetYear);
        model.addAttribute("currentMonth", targetMonth);
        model.addAttribute("todayDay", todayDay);
        model.addAttribute("calendarDays", calendarDays);
        model.addAttribute("hasTaskMap", hasTaskMap);

        // HTMLの◀▶ボタンで使うための、前月・翌月のデータを送る
        model.addAttribute("prevYear", prevYM.getYear());
        model.addAttribute("prevMonth", prevYM.getMonthValue());
        model.addAttribute("nextYear", nextYM.getYear());
        model.addAttribute("nextMonth", nextYM.getMonthValue());

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