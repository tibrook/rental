package com.chatop.rental.service;

import com.chatop.rental.dto.requests.MessageRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.model.Message;
import com.chatop.rental.model.Rental;
import com.chatop.rental.repository.MessageRepository;
import com.chatop.rental.repository.RentalRepository;
import com.chatop.rental.service.interfaces.MessageService;
import com.chatop.rental.service.interfaces.UserService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementation of MessageService interface providing message sending functionality.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserService userService;
    /**
     * Sends a message based on the provided request.
     * @param request MessageRequest object containing message details.
     * @return MessageResponse indicating the success of the message sending operation.
     */
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
    	Optional<Rental> rentalOptional = rentalRepository.findById(request.getRentalId());
        if (!rentalOptional.isPresent()) {
            // If no rental is found with the provided ID, return an appropriate response
            log.info("No rental found with ID: {}", request.getRentalId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found");
        }
        // Check if user exists. Otherwise, userService.getUserById throws exception
        userService.getUserById(Long.valueOf(request.getUserId()));      
        Message message = new Message();
        message.setMessage(request.getMessage());
        message.setUserId(request.getUserId());
        message.setRentalId(request.getRentalId());
        messageRepository.save(message);
        return new MessageResponse("Message send with success");
    }
}
