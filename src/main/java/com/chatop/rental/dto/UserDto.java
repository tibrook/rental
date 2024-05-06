package com.chatop.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor  
@AllArgsConstructor
@Getter            
@Setter        
public class UserDto {
    private Integer id;
    private String email;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
