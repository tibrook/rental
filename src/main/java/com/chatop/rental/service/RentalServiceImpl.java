package com.chatop.rental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.requests.CreateRentalRequest;
import com.chatop.rental.dto.requests.UpdateRentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailResponse;
import com.chatop.rental.model.Rental;
import com.chatop.rental.repository.RentalRepository;
import com.chatop.rental.service.interfaces.RentalService;
import com.chatop.rental.service.interfaces.StorageService;
import com.chatop.rental.service.interfaces.UserService;

/**
 * Implementation of RentalService interface providing rental management functionalities.
 */
@Service
public class RentalServiceImpl implements RentalService {
    private static final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private StorageService storageService;
    
    /**
     * Creates a new rental listing.
     * @param rentalRequest CreateRentalRequest object containing rental details.
     * @param userEmail Email of the user creating the rental listing.
     * @return Optional MessageResponse indicating the success of the rental creation.
     */
    @Override
    public Optional<MessageResponse> createRental(CreateRentalRequest rentalRequest, String userEmail) {
        return userService.findByEmail(userEmail).map(user -> {
            String imageUrl = storageService.store(rentalRequest.getPicture());
            Rental rental = new Rental();
            rental.setOwnerId(user.getId());
            rental.setName(rentalRequest.getName());
            rental.setSurface(rentalRequest.getSurface());
            rental.setPrice(rentalRequest.getPrice());
            rental.setDescription(rentalRequest.getDescription());
            rental.setPicture(imageUrl);
            rentalRepository.save(rental);
            log.info("Rental successfully created with ID {}", rental.getId());
            return new MessageResponse("Rental created!");
        });
    }
    /**
     * Retrieves all rentals.
     * @return List of RentalDto objects representing all rentals.
     */
    @Override
    public List<RentalDto> getAllRentals() {
        log.info("Fetching all rentals");
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                      .map(this::convertToDto)
                      .collect(Collectors.toList());
    }
    /**
     * Converts a Rental entity to its DTO representation.
     * @param rental Rental entity to be converted.
     * @return RentalDto representing the rental.
     */
    private RentalDto convertToDto(Rental rental) {
        RentalDto dto = modelMapper.map(rental, RentalDto.class);
        dto.setPicture(storageService.getFullImagePath(rental.getPicture()));
        return dto;
    }
    /**
     * Retrieves a rental by its ID.
     * @param rentalId ID of the rental to retrieve.
     * @return Optional RentalDetailResponse representing the rental details.
     */
    @Override
    public Optional<RentalDetailResponse> getRentalById(Integer rentalId) {
        log.info("Fetching rental with ID {}", rentalId);
        return rentalRepository.findById(rentalId)
        		.map(rental -> {
                    log.info("Rental found: {}", rental);
                    RentalDetailResponse response = modelMapper.map(rental, RentalDetailResponse.class);
                    List<String> pictures = Collections.singletonList(storageService.getFullImagePath(rental.getPicture()));
                    response.setPicture(pictures);                    
                    return response;
                });
    }
    /**
    /**
     * Updates a rental only if the authenticated user is the owner of the rental.
     * @param rentalId ID of the rental to update.
     * @param rentalRequest UpdateRentalRequest containing the updated rental details.
     * @param authentication Authentication object representing the currently authenticated user.
     * @return Optional<MessageResponse> indicating the success of the operation or empty if unauthorized.
     */
    @Override
    public Optional<MessageResponse> updateRental(Integer rentalId, UpdateRentalRequest rentalRequest, Authentication authentication) {
        return rentalRepository.findById(rentalId).map(rental -> {
            // Check if the authenticated user is the owner of the rental
        	Integer userId = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
                    .getId();
            if (!rental.getOwnerId().equals(userId)) {
                log.info("Update rental not authorized. Bad owner: {}", rental.getOwnerId(), authentication.getName());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to modify this rental");
            }
            rental.setName(rentalRequest.getName());
            rental.setSurface(rentalRequest.getSurface());
            rental.setPrice(rentalRequest.getPrice());
            rental.setDescription(rentalRequest.getDescription());
            rentalRepository.save(rental);
            return new MessageResponse("Rental updated!");
        });
    }

}
