package com.chatop.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String email;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
