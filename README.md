# Kafka CLI Chat Application

A command-line chat application using **Spring Boot** and **Apache Kafka**, where each chat room is a Kafka topic. Users can send/receive messages in real time.

## Features
- Real-time messaging using Kafka pub/sub.
- Dynamic Kafka topic creation per room.
- JSON serialization for structured messages.
- Minimalist CLI interface (no WebSocket/database).

## Tech Stack
- **Backend**: Spring Boot (Java)
- **Messaging**: Apache Kafka
- **Serialization**: JSON (Spring Kafka)
- **Tooling**: Lombok, Maven

### Use the CLI
1. Enter your username and start typing messages.
2. Open multiple terminals to simulate different users.

## Example Output
```plaintext
# Terminal 1
Enter your username: Alice
Hello world!

# Terminal 2
Enter your username: Bob
[Alice] Hello world!
Hi Alice!
```

https://github.com/user-attachments/assets/7176daf1-c580-4d57-94ba-2833570c2d0a

## Extensions
1. **Message History**: Persist messages to PostgreSQL/MongoDB.
2. **Error Handling**: Add dead-letter topics for failed messages.

---

### Key Notes:
1. **Simplicity**: Focuses on core Kafka pub/sub mechanics.
2. **Ready for Extensions**: Easy to add UIs, auth, or microservices later.
3. **Production-Friendly**: Uses Spring Kafka for robust configuration.
