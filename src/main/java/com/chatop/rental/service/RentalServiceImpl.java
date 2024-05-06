package com.chatop.rental.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.rental.dto.MessageResponse;
import com.chatop.rental.dto.RentalRequest;
import com.chatop.rental.model.Rental;
import com.chatop.rental.repository.RentalRepository;

@Service
public class RentalServiceImpl implements RentalService {
    private static final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Override
    public Optional<MessageResponse> createRental(RentalRequest rentalRequest, String userEmail) {
        return userService.findByEmail(userEmail).map(user -> {
            String imageUrl = storageService.store(rentalRequest.getPicture());
            Rental rental = new Rental();
            rental.setOwnerId(user.getId());
            rental.setName(rentalRequest.getName());
            rental.setSurface(rentalRequest.getSurface());
            rental.setPrice(rentalRequest.getPrice());
            rental.setDescription(rentalRequest.getDescription());
            rental.setPictureUrl(imageUrl);
            rentalRepository.save(rental);
            log.info("Rental successfully created with ID {}", rental.getId());
            return new MessageResponse("Rental created!");
        });
    }
}
