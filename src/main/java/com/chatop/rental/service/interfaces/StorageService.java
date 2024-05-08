package com.chatop.rental.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file);
    void init();
}