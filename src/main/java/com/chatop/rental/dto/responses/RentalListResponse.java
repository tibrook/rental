package com.chatop.rental.dto.responses;

import java.util.List;

import com.chatop.rental.dto.RentalDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalListResponse {
    private List<RentalDto> rentals;
}