package com.tcs.devops.challenge.message_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.devops.challenge.message_service.dto.RequestDTO;
import com.tcs.devops.challenge.message_service.dto.ResponseDTO;
import com.tcs.devops.challenge.message_service.service.MessageService;

@RestController
@RequestMapping("/DevOps")
public class MessageController {
    
    private final MessageService messageService;

    public MessageController(MessageService taskService) {
        this.messageService = taskService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> response(@RequestBody RequestDTO request) {
        String message = messageService.createMessage(request);
        ResponseDTO response = new ResponseDTO(message);
        return ResponseEntity.ok(response);
    }
    
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> handleOtherMethods() {
        return ResponseEntity.status(405).body("ERROR");
    }
    
}
