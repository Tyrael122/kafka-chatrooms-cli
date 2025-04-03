package org.contoso.kafkachatapplication.ports;

import org.contoso.kafkachatapplication.domain.model.ChatMessage;

public interface ChatMessageProducer {
    void sendMessage(String chatRoomId, ChatMessage message);
}
