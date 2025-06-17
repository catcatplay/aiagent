package com.nian.aiagent.controller;

import com.nian.aiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private LoveApp loveApp;

    @GetMapping
    public String chat(@RequestParam String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }
}
