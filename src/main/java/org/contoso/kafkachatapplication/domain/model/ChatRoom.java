package org.contoso.kafkachatapplication.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ChatRoom implements Serializable {
    private final String id = UUID.randomUUID().toString();

    private final String name;
}
