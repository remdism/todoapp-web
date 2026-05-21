package com.example.todoapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // このクラスが司令塔（Controller）であることをSpringに伝えます
public class TodoController {

    @GetMapping("/") // ブラウザでトップページ（/）にアクセスされたときにこのメソッドを動かします
    public String index() {
        return "index"; // templatesフォルダの中にある「index.html」を表示しろ、という指示になります
    }
}