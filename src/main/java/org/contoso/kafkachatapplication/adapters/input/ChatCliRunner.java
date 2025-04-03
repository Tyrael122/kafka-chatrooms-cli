package org.contoso.kafkachatapplication.adapters.input;

import org.contoso.kafkachatapplication.domain.model.ChatRoom;
import org.contoso.kafkachatapplication.domain.services.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ChatCliRunner implements CommandLineRunner {
    private final ChatService chatService;
    private final Scanner scanner = new Scanner(System.in);

    public ChatCliRunner(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to the Chat Room Application!");
        loginUser();
        showMainMenu();
    }

    private void loginUser() {
        System.out.print("Please enter your username: ");
        String user = scanner.nextLine().trim();
        System.out.println("Welcome, " + user + "!");

        chatService.login(user);
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Create a chat room");
            System.out.println("2. Enter a chat room");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createChatRoom();
                    break;
                case "2":
                    enterChatRoom();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createChatRoom() {
        System.out.print("Enter name for the new chat room: ");
        String roomName = scanner.nextLine().trim();

        try {
            chatService.createChatRoom(roomName);
            System.out.println("Chat room '" + roomName + "' created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating room: " + e.getMessage());
        }
    }

    private void enterChatRoom() {
        List<ChatRoom> availableRooms = chatService.getAllChatRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("No chat rooms available. Please create one first.");
            return;
        }

        System.out.println("\nAvailable Chat Rooms:");
        for (ChatRoom room : availableRooms) {
            System.out.println(" - " + room);
        }

        System.out.print("Enter the name of the chat room you want to join (or 'back' to return): ");
        String roomName = scanner.nextLine().trim();

        if (roomName.equalsIgnoreCase("back")) {
            return;
        }

        try {
            chatService.enterChatRoom(roomName);
            System.out.println("\nYou have entered chat room: " + roomName);
            showChatRoomMenu();
        } catch (Exception e) {
            System.out.println("Error entering room: " + e.getMessage());
        }
    }

    private void showChatRoomMenu() {
        System.out.println("Type EXIT anytime to leave this chat room");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("EXIT")) {
                leaveChatRoom();
                return;
            }

            sendMessage(input);
        }
    }

    private void sendMessage(String message) {
        try {
            chatService.sendMessage(message);
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    private void leaveChatRoom() {
        try {
            chatService.leaveChatRoom();
            System.out.println("You have left the chat room.");
        } catch (Exception e) {
            System.out.println("Error leaving room: " + e.getMessage());
        }
    }
}