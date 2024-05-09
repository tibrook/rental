package com.chatop.rental.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.requests.CreateRentalRequest;
import com.chatop.rental.dto.requests.UpdateRentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailResponse;

public interface RentalService {
	Optional<MessageResponse> createRental(CreateRentalRequest rentalRequest, String userEmail);
	List<RentalDto> getAllRentals();
    Optional<RentalDetailResponse> getRentalById(Integer rentalId);
    Optional<MessageResponse> updateRental(Integer rentalId, UpdateRentalRequest rentalRequest);
}