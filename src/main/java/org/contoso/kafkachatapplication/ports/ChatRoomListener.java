package org.contoso.kafkachatapplication.ports;

import org.contoso.kafkachatapplication.domain.model.ChatMessage;
import org.contoso.kafkachatapplication.domain.model.ChatRoom;

import java.util.function.Consumer;

public interface ChatRoomListener {
    void enterChatRoom(ChatRoom room, Consumer<ChatMessage> listener);
    void leaveChatRoom(String id);
}
