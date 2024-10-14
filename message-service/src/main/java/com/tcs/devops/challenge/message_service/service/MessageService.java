package com.tcs.devops.challenge.message_service.service;

import org.springframework.stereotype.Service;

import com.tcs.devops.challenge.message_service.dto.RequestDTO;

@Service
public class MessageService {
    public String createMessage(RequestDTO request) {
        return String.format("Hello %s your message will be send", request.getTo());
    }
}
