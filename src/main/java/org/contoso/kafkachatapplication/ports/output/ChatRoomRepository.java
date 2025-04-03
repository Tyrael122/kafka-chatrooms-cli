package org.contoso.kafkachatapplication.ports.output;

import org.contoso.kafkachatapplication.domain.model.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {
    void saveChatRoom(ChatRoom chatRoom);
    ChatRoom findChatRoomById(String id);
    List<ChatRoom> findAllChatRooms();
}
