package com.toronto.opendata.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cultural_hotspots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalHotSpotEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "csv_id")
    private String csvId;
    
    @Column(name = "loops_guide")
    private String loopsGuide;
    
    @Column(name = "loop")
    private String loop;
    
    @Column(name = "tour_num")
    private String tourNum;
    
    @Column(name = "order_num")
    private String orderNum;
    
    @Column(name = "loop_tour_name")
    private String loopTourName;
    
    @Column(name = "loop_tour_url", length = 500)
    private String loopTourUrl;
    
    @Column(name = "tour_label")
    private String tourLabel;
    
    @Column(name = "site_name")
    private String siteName;
    
    @Column(name = "neighbourhood")
    private String neighbourhood;
    
    @Column(name = "tour_type")
    private String tourType;
    
    @Column(name = "duration")
    private String duration;
    
    @Column(name = "entry_type")
    private String entryType;
    
    @Column(name = "interests")
    private String interests;
    
    @Column(name = "directions_transit", length = 1000)
    private String directionsTransit;
    
    @Column(name = "directions_car", length = 1000)
    private String directionsCar;
    
    @Column(name = "description", length = 5000)
    private String description;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "external_link", length = 500)
    private String externalLink;
    
    @Column(name = "image_credit")
    private String imageCredit;
    
    @Column(name = "image_credit_external_link", length = 500)
    private String imageCreditExternalLink;
    
    @Column(name = "image_alt_text", length = 1000)
    private String imageAltText;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "thumb_url", length = 500)
    private String thumbUrl;
    
    @Column(name = "image_orientation")
    private String imageOrientation;
    
    @Column(name = "test1")
    private String test1;
    
    @Column(name = "test2")
    private String test2;
    
    @Column(name = "test3")
    private String test3;
    
    @Column(name = "object_id")
    private String objectId;
    
    // Geometry stored as JSON text for now (can be upgraded to PostGIS later)
    @Column(name = "geometry", columnDefinition = "TEXT")
    private String geometry;
    
    // Parsed coordinates for easy querying
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
