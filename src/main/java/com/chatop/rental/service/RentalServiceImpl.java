package com.chatop.rental.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.requests.CreateRentalRequest;
import com.chatop.rental.dto.requests.UpdateRentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailResponse;
import com.chatop.rental.model.Rental;
import com.chatop.rental.repository.RentalRepository;
import com.chatop.rental.service.interfaces.RentalService;
import com.chatop.rental.service.interfaces.UserService;

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
    @Override
    public List<RentalDto> getAllRentals() {
        log.info("Fetching all rentals");
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                      .map(this::convertToDto)
                      .collect(Collectors.toList());
    }

    private RentalDto convertToDto(Rental rental) {
        return modelMapper.map(rental, RentalDto.class);
    }
    @Override
    public Optional<RentalDetailResponse> getRentalById(Integer rentalId) {
        log.info("Fetching rental with ID {}", rentalId);
        return rentalRepository.findById(rentalId)
                .map(rental -> {
                    log.info("Rental found: {}", rental);  
                    return modelMapper.map(rental, RentalDetailResponse.class);
                });
    }
    @Override
    public Optional<MessageResponse> updateRental(Integer rentalId, UpdateRentalRequest rentalRequest) {
        return rentalRepository.findById(rentalId).map(rental -> {
            rental.setName(rentalRequest.getName());
            rental.setSurface(rentalRequest.getSurface());
            rental.setPrice(rentalRequest.getPrice());
            rental.setDescription(rentalRequest.getDescription());
            rentalRepository.save(rental);
            return new MessageResponse("Rental updated!");
        });
    }

}
