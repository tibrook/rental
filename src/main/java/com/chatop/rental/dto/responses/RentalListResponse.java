package com.chatop.rental.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalListResponse {
    private List<RentalDto> rentals;
}