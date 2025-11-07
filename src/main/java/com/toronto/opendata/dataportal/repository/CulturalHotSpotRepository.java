package com.toronto.opendata.dataportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toronto.opendata.dataportal.model.CulturalHotSpot;

@Repository
public interface CulturalHotSpotRepository extends JpaRepository<CulturalHotSpot, Long> {
    
    // TODO: When database is implemented, these methods will be ready
    
    // Pagination is built-in from JpaRepository:
    // Page<CulturalHotSpot> findAll(Pageable pageable);
    
    // TODO: Add pagination to search methods
    Page<CulturalHotSpot> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<CulturalHotSpot> findByCategory(String category, Pageable pageable);
    
    Page<CulturalHotSpot> findByType(String type, Pageable pageable);
}
