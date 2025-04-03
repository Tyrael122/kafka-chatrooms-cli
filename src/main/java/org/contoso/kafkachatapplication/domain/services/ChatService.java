package org.contoso.kafkachatapplication.domain.services;

import lombok.RequiredArgsConstructor;
import org.contoso.kafkachatapplication.domain.model.ChatMessage;
import org.contoso.kafkachatapplication.domain.model.ChatRoom;
import org.contoso.kafkachatapplication.ports.ChatMessageProducer;
import org.contoso.kafkachatapplication.ports.ChatRoomListener;
import org.contoso.kafkachatapplication.ports.output.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageProducer chatMessageProducer;
    private final ChatRoomListener chatRoomListener;
    private final ChatRoomRepository chatRoomRepository;

    private ChatRoom currentChatRoom;

    private String currentUser;

    public void login(String user) {
        currentUser = user;
    }

    public void sendMessage(String message) {
        if (currentChatRoom == null) {
            throw new IllegalStateException("Chat room not yet initialized");
        }

        ChatMessage chatMessage = new ChatMessage(currentUser, message);

        chatMessageProducer.sendMessage(currentChatRoom.getId(), chatMessage);
    }

    public void enterChatRoom(String room) {

        // find room with same name using java streams.
        ChatRoom chatRoom = chatRoomRepository.findAllChatRooms().stream()
                .filter(chatRoom1 -> chatRoom1.getName().equals(room))
                .findFirst()
                .orElseGet(() -> createChatRoom(room));

        chatRoomListener.enterChatRoom(chatRoom,
                (message) -> {
                    if (message.getUsername().equals(currentUser)) {
                        return;
                    }

                    System.out.printf("[%s] %s\n", message.getUsername(), message.getText());
                });

        currentChatRoom = chatRoom;
    }

    public void leaveChatRoom() {
        chatRoomListener.leaveChatRoom(currentChatRoom.getId());
    }

    public ChatRoom createChatRoom(String roomName) {
        for (ChatRoom chatRoom : chatRoomRepository.findAllChatRooms()) {
            if (chatRoom.getName().equals(roomName)) {
                return chatRoom;
            }
        }

        ChatRoom room = new ChatRoom(roomName);

        chatRoomRepository.saveChatRoom(room);

        return room;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAllChatRooms();
    }
}
