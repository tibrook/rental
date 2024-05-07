package com.chatop.rental.service;

import java.util.List;
import java.util.Optional;

import com.chatop.rental.dto.MessageResponse;
import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.RentalRequest;

public interface RentalService {
	Optional<MessageResponse> createRental(RentalRequest rentalRequest, String userEmail);
	List<RentalDto> getAllRentals();

}