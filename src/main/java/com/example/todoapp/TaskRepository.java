package com.example.todoapp;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepositoryを継承するだけで、保存（save）や全件取得（findAll）の機能が自動的に使えるようになります
public interface TaskRepository extends JpaRepository<Task, Integer> {
}