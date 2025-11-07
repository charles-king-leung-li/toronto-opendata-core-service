package com.toronto.opendata.dataportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toronto.opendata.dataportal.model.CulturalHotSpot;

@Repository
public interface CulturalHotSpotRepository extends JpaRepository<CulturalHotSpot, Long> {
    
    // Custom query methods
    List<CulturalHotSpot> findByCategory(String category);
    
    List<CulturalHotSpot> findByType(String type);
    
    List<CulturalHotSpot> findByNameContainingIgnoreCase(String name);
}
