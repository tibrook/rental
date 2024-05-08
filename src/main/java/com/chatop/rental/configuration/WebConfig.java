package com.chatop.rental.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	/**
     * Configures a resource handler for serving static files.
     * This method is part of the WebMvcConfigurer interface and is used to customize the way static resources are handled.
     * 
     * @param registry The ResourceHandlerRegistry used to register resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Registers a resource handler for URLs matching '/uploads/**'
        registry.addResourceHandler("/uploads/**")
    		// Specifies the physical path to the resources, prefixed with 'file:' to indicate that these are file resources.
        	.addResourceLocations("file:uploads/");  
    }
}
