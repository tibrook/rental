package com.chatop.rental.service.interfaces;

import com.chatop.rental.dto.requests.MessageRequest;
import com.chatop.rental.dto.responses.MessageResponse;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest request);
}
