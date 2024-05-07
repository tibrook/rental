package com.chatop.rental.dto.responses;

import java.util.Date;

import com.chatop.rental.serialization.CustomDoubleSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {
    private Integer id;
    private String name;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double surface;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double price;    
    private String picture;
    private String description;
    
    @JsonProperty("owner_id")
    private Integer ownerId;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "UTC")
    private Date updatedAt;
}