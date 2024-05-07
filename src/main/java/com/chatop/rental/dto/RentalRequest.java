package com.chatop.rental.dto;

import org.springframework.web.multipart.MultipartFile;

import com.chatop.rental.validations.ValidImage;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RentalRequest {
 	@NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Surface cannot be null")
    @Min(value = 1, message = "Surface must be at least 1 square meter")
    private Double surface;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.1", message = "Minimum price must be at least 0.1")
    private Double price;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 2048, message = "Description cannot exceed 2048 characters")
    private String description;

    @NotNull(message = "Picture cannot be null")
    @ValidImage
    private MultipartFile picture;
}