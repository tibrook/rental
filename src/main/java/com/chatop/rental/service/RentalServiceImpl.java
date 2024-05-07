package com.chatop.rental.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.rental.dto.requests.RentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDto;
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
    private ModelMapper modelMapper;
    
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
}
