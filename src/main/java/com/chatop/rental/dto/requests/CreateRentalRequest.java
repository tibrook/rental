package com.chatop.rental.dto.requests;

import org.springframework.web.multipart.MultipartFile;

import com.chatop.rental.validations.ValidImage;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRentalRequest {
    @Size(max = 255, message = "Name cannot exceed 255 characters")
 	@NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Surface cannot be null")
    @DecimalMin(value = "1.0", message = "Surface must be at least 1.0 square meter")
    @Digits(integer = 8, fraction = 2, message = "Surface must not exceed 8 digits in total with up to 2 decimal places")
    private Double surface;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 8, fraction = 2, message = "Price must not exceed 8 digits in total with up to 2 decimal places")
    private Double price;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 2048, message = "Description cannot exceed 2048 characters")
    private String description;

    @NotNull(message = "Picture cannot be null")
    @ValidImage
    private MultipartFile picture;
}