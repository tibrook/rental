package com.chatop.rental.dto;

import java.util.Date;

public class UserDto {
    private Integer id;
    private String email;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    public UserDto(Integer id, String email, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
