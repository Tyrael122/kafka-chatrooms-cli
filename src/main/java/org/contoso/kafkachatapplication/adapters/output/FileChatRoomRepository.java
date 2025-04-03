package org.contoso.kafkachatapplication.adapters.output;

import org.contoso.kafkachatapplication.domain.model.ChatRoom;
import org.contoso.kafkachatapplication.ports.output.ChatRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileChatRoomRepository implements ChatRoomRepository {
    private static final String STORAGE_DIR = "chatrooms";
    private static final String FILE_EXTENSION = ".chatroom";

    public FileChatRoomRepository() {
        // Ensure storage directory exists
        Path path = Paths.get(STORAGE_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create storage directory", e);
            }
        }
    }

    @Override
    public void saveChatRoom(ChatRoom chatRoom) {
        String filename = STORAGE_DIR + File.separator + chatRoom.getId() + FILE_EXTENSION;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(chatRoom);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save chat room", e);
        }
    }

    @Override
    public ChatRoom findChatRoomById(String id) {
        String filename = STORAGE_DIR + File.separator + id + FILE_EXTENSION;
        File file = new File(filename);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ChatRoom) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read chat room", e);
        }
    }

    @Override
    public List<ChatRoom> findAllChatRooms() {
        File folder = new File(STORAGE_DIR);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));

        if (files == null) {
            return List.of();
        }

        return Arrays.stream(files)
                .map(file -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        return (ChatRoom) ois.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException("Failed to read chat room from file: " + file.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }
}