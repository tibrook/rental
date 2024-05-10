package com.chatop.rental.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.rental.exception.BadRequestException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Validator for validating image files.
 */
public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
	private static final int MAX_FILENAME_LENGTH = 255;
    private static final Logger log = LoggerFactory.getLogger(ImageFileValidator.class);

    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }
    /**
     * Validates if the provided file is a valid image file.
     * @param file MultipartFile to be validated.
     * @param context ConstraintValidatorContext providing contextual data and operation when applying the constraint.
     * @return true if the file is a valid image, false otherwise.
     */
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    	if(file == null) {
    		log.error("File is required");
    		throw new BadRequestException();
    	}
        String filename = file.getOriginalFilename();
        if (filename != null && filename.length() > MAX_FILENAME_LENGTH) {
        	log.error("Picture filename cannot exceed 255 characters");
    		throw new BadRequestException();
        }

        String contentType = file.getContentType();
        boolean isImage = contentType != null && contentType.matches("image/.*");
        if (!isImage) {
        	log.error("Invalid file type, only images are allowed");
    		throw new BadRequestException();
        }
        return isImage;
    }
}