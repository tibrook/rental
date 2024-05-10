package com.chatop.rental.service;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.dto.requests.MessageRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.exception.BadRequestException;
import com.chatop.rental.model.Message;
import com.chatop.rental.model.Rental;
import com.chatop.rental.repository.MessageRepository;
import com.chatop.rental.repository.RentalRepository;
import com.chatop.rental.service.interfaces.MessageService;
import com.chatop.rental.service.interfaces.UserService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of MessageService interface providing message sending functionality.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserService userService;
    
    public MessageServiceImpl(UserService userService, MessageRepository messageRepository, RentalRepository rentalRepository) {
    	this.userService = userService;
    	this.messageRepository=messageRepository;
    	this.rentalRepository = rentalRepository;
    }
   
    /**
     * Sends a message based on the provided request.
     * @param request MessageRequest object containing message details.
     * @return MessageResponse indicating the success of the message sending operation.
     */
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
    	Optional<Rental> rentalOptional = rentalRepository.findById(request.getRentalId());
        if (!rentalOptional.isPresent()) {
            log.error("No rental found with ID: {}", request.getRentalId());
            throw new BadRequestException();
        }
        Optional<UserDto> userOptional = userService.getUserById(Long.valueOf(request.getUserId()));      
        if(!userOptional.isPresent()) {
            log.error("No user found with ID: {}", request.getUserId());
            throw new BadRequestException();
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
