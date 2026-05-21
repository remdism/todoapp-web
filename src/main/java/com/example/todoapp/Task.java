package com.example.todoapp;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String title;
    private boolean isCompleted;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd") // HTMLの「年-月-日」形式をJavaの日付型に変換
    private LocalDate dueDate;

    // JPA用の空のコンストラクタ
    public Task() {}

    // コンストラクタ
    public Task(String title, boolean isCompleted, LocalDate dueDate) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.dueDate = dueDate;
    }

    // Getter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return isCompleted; }
    
    // Setter
    public void setCompleted(boolean completed) { isCompleted = completed; }

    // dueDateのGetterとSetter
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}