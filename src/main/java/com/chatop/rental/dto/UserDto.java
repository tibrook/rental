package com.chatop.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor  
@AllArgsConstructor
@Getter            
@Setter        
public class UserDto {
    private Integer id;
    private String email;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    private Date updatedAt;
}
