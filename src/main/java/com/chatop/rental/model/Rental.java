package com.chatop.rental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

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
    private Integer surface;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String pictureUrl;

    @Column(nullable = false, length = 2048)
    private String description;
    
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId; 
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;
}
