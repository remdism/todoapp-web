package com.example.todoapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // データベースのテーブル
public class Task {
    
    @Id // 主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String title;
    private boolean isCompleted;

    // 空のコンストラクタ
    public Task() {}

    // コンストラクタ
    public Task(String title, boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    // Getter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return isCompleted; }
    
    // Setter
    public void setCompleted(boolean completed) { isCompleted = completed; }
}