package org.contoso.kafkachatapplication.adapters.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.contoso.kafkachatapplication.domain.model.ChatMessage;
import org.contoso.kafkachatapplication.domain.model.ChatRoom;
import org.contoso.kafkachatapplication.ports.ChatRoomListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class KafkaChatRoomListener implements ChatRoomListener {

    private final ConsumerFactory<String, String> consumerFactory;

    private final Map<String, ConcurrentMessageListenerContainer<String, String>> containers = new ConcurrentHashMap<>();

    public KafkaChatRoomListener(ConsumerFactory<String, String> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    @Override
    public void enterChatRoom(ChatRoom room, Consumer<ChatMessage> listener) {
        subscribe(room.getId(), UUID.randomUUID().toString(), message -> {
            try {
                ChatMessage chatMessage = new ObjectMapper().readValue(message, ChatMessage.class);
                listener.accept(chatMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void leaveChatRoom(String id) {
        unsubscribe(id);
    }

    public void subscribe(String topic, String groupId, Consumer<String> messageConsumer) {
        if (containers.containsKey(topic)) {
            return; // already subscribed
        }

        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setGroupId(groupId);

        ConcurrentMessageListenerContainer<String, String> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);

        container.setupMessageListener((MessageListener<String, String>) record ->
                messageConsumer.accept(record.value()));

        container.start();
        containers.put(topic, container);
    }

    public void unsubscribe(String topic) {
        ConcurrentMessageListenerContainer<String, String> container = containers.remove(topic);
        if (container != null) {
            container.stop();
        }
    }
}