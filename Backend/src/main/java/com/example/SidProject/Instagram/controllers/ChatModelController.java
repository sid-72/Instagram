package com.example.SidProject.Instagram.controllers;

import com.example.SidProject.Instagram.Services.ChatModelService;
import lombok.AllArgsConstructor;
//import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ChatModelController {
    private ChatModelService chatModelService;


    @GetMapping("/chatmodel")
    public String getChatModel(@RequestParam String userId,
                               @RequestParam String conversationId,
                               @RequestParam String message) {
        ChatResponse response = chatModelService.chat(userId, conversationId, message);
        return response.getResult().getOutput().getText();
    }
}
