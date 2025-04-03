package org.contoso.kafkachatapplication.adapters.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.contoso.kafkachatapplication.domain.model.ChatMessage;
import org.contoso.kafkachatapplication.ports.ChatMessageProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaChatMessageProducer implements ChatMessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaChatMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String chatRoomId, ChatMessage message) {
        try {
            String messageAsString = new ObjectMapper().writeValueAsString(message);
            kafkaTemplate.send(chatRoomId, messageAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}