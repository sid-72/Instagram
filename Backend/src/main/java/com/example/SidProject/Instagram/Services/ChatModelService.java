package com.example.SidProject.Instagram.Services;

import com.google.genai.Chat;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatModelService {
    private GoogleGenAiChatModel chatModel;
    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;

    public ChatResponse chat(String userId, String conversationId, String message) {
        try {
            String chatId = userId + ":" + conversationId;
            ChatMemory chatMemory = MessageWindowChatMemory.builder()
                    .chatMemoryRepository(chatMemoryRepository)
                    .maxMessages(10) // window size
                    .build();
            UserMessage userMessage1 = new UserMessage(message);
            //chatMemory.add(chatId, userMessage1);
            Prompt prompt = Prompt.builder()
                    .messages(userMessage1)
                    .build();
            ChatResponse chatResponse = chatModel.call(prompt);
            //chatMemory.add(chatId, chatResponse.getResult().getOutput());
            return  chatResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error in chat", e);
        }
    }
}
