package com.chatop.rental.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.chatop.rental.serialization.CustomBigDecimalSerializer;
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
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal surface;
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal price;    
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