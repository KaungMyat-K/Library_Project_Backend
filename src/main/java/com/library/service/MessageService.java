package com.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dto.AdminQuestionDto;
import com.library.entity.MessageEntity;
import com.library.repository.MessageRepository;

@Service
public class MessageService {
    

    @Autowired
    private MessageRepository messageRepository;

    public void postMessage(MessageEntity messageRequest, String userEmail) {
        MessageEntity message = 
        MessageEntity.builder().title(messageRequest.getTitle()).question(messageRequest.getQuestion()).userEmail(userEmail).build();
        messageRepository.save(message);
    }

    public void putMessage(AdminQuestionDto adminQuestionRequest, String userEmail) throws Exception {
        Optional<MessageEntity> message = messageRepository.findById(adminQuestionRequest.getId());
        if (!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }
}
