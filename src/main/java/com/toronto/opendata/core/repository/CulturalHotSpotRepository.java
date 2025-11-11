package com.toronto.opendata.core.repository;

import com.toronto.opendata.core.entity.CulturalHotSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CulturalHotSpotRepository extends JpaRepository<CulturalHotSpotEntity, Long> {
    
    /**
     * Find hotspot by CSV ID
     */
    Optional<CulturalHotSpotEntity> findByCsvId(String csvId);
    
    /**
     * Find all hotspots in a specific neighbourhood
     */
    List<CulturalHotSpotEntity> findByNeighbourhood(String neighbourhood);
    
    /**
     * Find hotspots by tour type
     */
    List<CulturalHotSpotEntity> findByTourType(String tourType);
    
    /**
     * Find hotspots by interests (contains search)
     */
    List<CulturalHotSpotEntity> findByInterestsContainingIgnoreCase(String interest);
    
    /**
     * Find hotspots within a bounding box (for map queries)
     */
    @Query("SELECT h FROM CulturalHotSpotEntity h WHERE " +
           "h.latitude BETWEEN :minLat AND :maxLat AND " +
           "h.longitude BETWEEN :minLon AND :maxLon")
    List<CulturalHotSpotEntity> findWithinBoundingBox(
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon
    );
    
    /**
     * Search by site name
     */
    List<CulturalHotSpotEntity> findBySiteNameContainingIgnoreCase(String siteName);
    
    /**
     * Find all hotspots in a specific loop
     */
    List<CulturalHotSpotEntity> findByLoop(String loop);
}
