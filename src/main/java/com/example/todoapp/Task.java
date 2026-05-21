package com.example.todoapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // ①このクラスをデータベースのテーブルとして扱う目印です
public class Task {
    
    @Id // ②この項目が主キー（Primary Key）であることの宣言です
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDを自動で連番（1, 2, 3...）にします
    private int id;
    
    private String title;
    private boolean isCompleted;

    // JPAの仕様上、空のコンストラクタが必須になります
    public Task() {}

    // 以前作ったコンストラクタ（IDは自動生成されるため引数から外します）
    public Task(String title, boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    // --- 以下のGetterはそのまま残しておきます ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return isCompleted; }
    
    // 値を更新するためのSetterも追加しておきます
    public void setCompleted(boolean completed) { isCompleted = completed; }
}