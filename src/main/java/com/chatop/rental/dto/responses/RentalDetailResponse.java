package com.chatop.rental.dto.responses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDetailResponse {
    private Integer id;
    private String name;
    private Integer surface;
    private Integer price;
    private List<String> picture;
    private String description;
    
    private Integer ownerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    private Date updatedAt;
}