package com.chatop.rental.service;

import java.util.List;
import java.util.Optional;

import com.chatop.rental.dto.requests.RentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailDto;
import com.chatop.rental.dto.responses.RentalDto;

public interface RentalService {
	Optional<MessageResponse> createRental(RentalRequest rentalRequest, String userEmail);
	List<RentalDto> getAllRentals();
    Optional<RentalDetailDto> getRentalById(Integer rentalId);
}