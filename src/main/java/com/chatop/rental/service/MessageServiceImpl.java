package com.chatop.rental.service;

import com.chatop.rental.dto.requests.MessageRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.model.Message;
import com.chatop.rental.repository.MessageRepository;
import com.chatop.rental.service.interfaces.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        Message message = new Message();
        message.setMessage(request.getMessage());
        message.setUserId(request.getUserId());
        message.setRentalId(request.getRentalId());
        messageRepository.save(message);
        return new MessageResponse("Message send with success");
    }
}
