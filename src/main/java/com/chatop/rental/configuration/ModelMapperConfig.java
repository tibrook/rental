package com.chatop.rental.configuration;

import java.util.Collections;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chatop.rental.dto.responses.RentalDetailDto;
import com.chatop.rental.model.Rental;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
        // Set strict matching strategy to ensure explicit mapping definitions are respected
	    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // Configure custom mappings for Rental to RentalDetailDto, specifically for the picture field
	    modelMapper.typeMap(Rental.class, RentalDetailDto.class).addMappings(mapper -> 
        	// Use the custom converter to map a single String picture to a List of Strings
	        mapper.using(toStringListConverter).map(Rental::getPicture, RentalDetailDto::setPicture)
	    );

		    return modelMapper;
	}
    // Define a converter from String to List<String> to handle the mapping of the 'picture' field
	Converter<String, List<String>> toStringListConverter = new Converter<String, List<String>>() {
	    public List<String> convert(MappingContext<String, List<String>> context) {
	    	// If source is null, return an empty list, otherwise wrap the source string in a singleton list
            // This is necessary because the DTO expects a list of pictures, even if there's only one picture URL.
	        return context.getSource() == null ? Collections.emptyList() : Collections.singletonList(context.getSource());
	    }
	};
}
