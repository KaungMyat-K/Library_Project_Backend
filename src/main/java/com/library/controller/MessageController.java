package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.Utils.ExtractJwt;
import com.library.dto.AdminQuestionDto;
import com.library.entity.MessageEntity;
import com.library.service.MessageService;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @Autowired
    private ExtractJwt extractJwt;

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value="Authorization") String token,
                            @RequestBody MessageEntity messageRequest) {
        String userEmail = extractJwt.payloadJWTExtraction(token, "\"sub\"");
        messageService.postMessage(messageRequest, userEmail);
    }

       @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value="Authorization") String token,@RequestBody AdminQuestionDto adminQuestionDto)throws Exception {
        String userEmail = extractJwt.payloadJWTExtraction(token, "\"sub\"");
        String admin = extractJwt.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only.");
        }
        messageService.putMessage(adminQuestionDto, userEmail);
    }


}
