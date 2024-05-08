package com.chatop.rental.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.rental.controller.advice.StorageException;
import com.chatop.rental.service.interfaces.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Implementation of StorageService interface providing file storage functionalities.
 */
@Service
public class StorageServiceImpl implements StorageService{

    private final Path rootLocation = Paths.get("uploads");
    @Value("${server.base-url}")
    private String baseUrl;
    /**
     * Gets the full image path based on the filename.
     * @param filename Name of the file.
     * @return Full image path.
     */
    public String getFullImagePath(String filename) {
        return baseUrl + filename;
    }
    /**
     * Stores the uploaded file.
     * @param file Uploaded file.
     * @return Relative path of the stored file.
     * @throws StorageException if storing the file fails.
     */
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
    /**
     * Initializes the storage directory.
     * @throws StorageException if initialization fails.
     */
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
