package com.chatop.rental.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "rentals")
@Data
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private BigDecimal surface;
    
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false, length = 2048)
    private String description;
    
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId; 
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Timestamp updatedAt;
}
