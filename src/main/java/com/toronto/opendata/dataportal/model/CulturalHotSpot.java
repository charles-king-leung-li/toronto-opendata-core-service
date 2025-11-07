package com.toronto.opendata.dataportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cultural_hot_spots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalHotSpot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private String category;
    
    private String website;
    
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(name = "picture_url")
    private String pictureUrl;
    
    private String type;
}
