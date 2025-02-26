package com.springAI.SpringAI.messageController;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return chatClient
                .prompt() // Start building the prompt
                .user(message) // Add user input
                .call() // Call the OpenAI API
                .content(); // Get the response content
    }
}
