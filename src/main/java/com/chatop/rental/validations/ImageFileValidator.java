package com.chatop.rental.validations;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
	private static final int MAX_FILENAME_LENGTH = 255;
	
    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.length() > MAX_FILENAME_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Picture filename cannot exceed 255 characters")
                   .addConstraintViolation();
            return false;
        }

        String contentType = file.getContentType();
        boolean isImage = contentType != null && contentType.matches("image/.*");
        if (!isImage) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid file type, only images are allowed")
                   .addConstraintViolation();
        }
        return isImage;
    }
}