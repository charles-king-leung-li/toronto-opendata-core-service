package com.toronto.opendata.dataportal.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.toronto.opendata.dataportal.model.CulturalHotSpot;
import com.toronto.opendata.dataportal.model.CulturalHotSpotModel;
import com.toronto.opendata.dataportal.model.MultiPointModel;
import com.toronto.opendata.dataportal.repository.CulturalHotSpotRepository;
import com.toronto.opendata.dataportal.util.GeometryParser;

@Service
public class CulturalHotSpotService {

    private CSVReaderService csvReaderService;
    private CulturalHotSpotRepository repository;
    private String csvFilePath = "data/points-of-interest-05-11-2025.csv";
    
    @Autowired
    public CulturalHotSpotService(CSVReaderService csvReaderService, CulturalHotSpotRepository repository) {
        this.csvReaderService = csvReaderService;
        this.repository = repository;
    }

    // ========== CSV Methods (Temporary) ==========
    
    public List<CulturalHotSpotModel> getCulturalHotSpots() {
        List<CulturalHotSpotModel> culturalHotSpots = new ArrayList<>();
        
        try {
            // Read CSV data
            List<Map<String, String>> csvdata = csvReaderService.readCSV(csvFilePath);
            
            // Transform each CSV record to CulturalHotSpotModel
            for (Map<String, String> row : csvdata) {
                CulturalHotSpotModel hotSpot = new CulturalHotSpotModel();
                
                // Map CSV columns to model fields (using actual CSV column names)
                hotSpot.setId(row.get("_id"));
                hotSpot.setName(row.get("SiteName"));
                hotSpot.setAddress(row.get("Address"));
                hotSpot.setType(row.get("TourType"));
                hotSpot.setDescription(row.get("Description"));
                hotSpot.setPictureURL(row.get("ImageURL"));
                
                // Parse geometry JSON string to MultiPointModel
                String geometryJson = row.get("geometry");
                if (geometryJson != null && !geometryJson.trim().isEmpty()) {
                    MultiPointModel location = GeometryParser.parseGeometrySafe(geometryJson);
                    hotSpot.setLocation(location);
                }
                
                culturalHotSpots.add(hotSpot);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read cultural hot spots from CSV", e);
        }
        
        return culturalHotSpots;
    }

    public CulturalHotSpotModel getCulturalHotSpotById(String id) {
        List<CulturalHotSpotModel> hotSpots = getCulturalHotSpots();
        for (CulturalHotSpotModel spot : hotSpots) {
            if (spot.getId() != null && spot.getId().equals(id)) {
                return spot;
            }
        }
        return null; // Not found
    }
    
    // ========== Database Methods (Future Implementation) ==========
    
    /**
     * TODO: Implement database pagination
     * Get all cultural hotspots with pagination
     * @param pageable Pagination information (page, size, sort)
     * @return Page of CulturalHotSpot entities
     */
    public Page<CulturalHotSpot> getAllHotSpotsFromDb(Pageable pageable) {
        // TODO: Migrate CSV data to database first
        // TODO: Then uncomment this line:
        // return repository.findAll(pageable);
        throw new UnsupportedOperationException("Database implementation pending - use CSV endpoints for now");
    }
    
    /**
     * TODO: Implement database retrieval by ID
     * Get a cultural hotspot by ID from database
     * @param id The hotspot ID
     * @return CulturalHotSpot entity
     */
    public CulturalHotSpot getHotSpotByIdFromDb(Long id) {
        // TODO: Migrate CSV data to database first
        // TODO: Then uncomment this line:
        // return repository.findById(id)
        //     .orElseThrow(() -> new ResourceNotFoundException("Cultural Hot Spot not found with id: " + id));
        throw new UnsupportedOperationException("Database implementation pending - use CSV endpoints for now");
    }
    
    /**
     * TODO: Implement search with pagination
     * Search hotspots by name with pagination
     * @param name Search term
     * @param pageable Pagination information
     * @return Page of matching CulturalHotSpot entities
     */
    public Page<CulturalHotSpot> searchByNameFromDb(String name, Pageable pageable) {
        // TODO: Add this method to repository:
        // Page<CulturalHotSpot> findByNameContainingIgnoreCase(String name, Pageable pageable);
        // TODO: Then implement here
        throw new UnsupportedOperationException("Database implementation pending");
    }

}