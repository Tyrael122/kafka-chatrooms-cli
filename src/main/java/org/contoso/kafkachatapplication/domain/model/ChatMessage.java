package org.contoso.kafkachatapplication.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChatMessage {
    private String username;
    private String text;
    private long timestamp = System.currentTimeMillis();

    public ChatMessage(String currentUser, String message) {
        this.username = currentUser;
        this.text = message;
    }
}