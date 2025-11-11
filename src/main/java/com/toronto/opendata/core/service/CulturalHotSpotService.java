package com.toronto.opendata.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.toronto.opendata.core.entity.CulturalHotSpotEntity;
import com.toronto.opendata.core.model.CulturalHotSpotModel;
import com.toronto.opendata.core.model.MultiPointModel;
import com.toronto.opendata.core.repository.CulturalHotSpotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CulturalHotSpotService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final CulturalHotSpotRepository repository;
    
    /**
     * Get all cultural hotspots from database
     */
    public List<CulturalHotSpotModel> getAllCulturalHotSpots() {
        log.debug("Fetching all cultural hotspots from database");
        List<CulturalHotSpotEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Get cultural hotspot by ID
     */
    public CulturalHotSpotModel getCulturalHotSpotById(String id) {
        log.debug("Fetching cultural hotspot with ID: {}", id);
        return repository.findByCsvId(id)
                .map(this::entityToModel)
                .orElse(null);
    }
    
    /**
     * Get hotspots by neighbourhood
     */
    public List<CulturalHotSpotModel> getHotSpotsByNeighbourhood(String neighbourhood) {
        log.debug("Fetching hotspots in neighbourhood: {}", neighbourhood);
        return repository.findByNeighbourhood(neighbourhood).stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Search hotspots by interest
     */
    public List<CulturalHotSpotModel> searchByInterest(String interest) {
        log.debug("Searching hotspots by interest: {}", interest);
        return repository.findByInterestsContainingIgnoreCase(interest).stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert entity to model
     */
    private CulturalHotSpotModel entityToModel(CulturalHotSpotEntity entity) {
        CulturalHotSpotModel model = new CulturalHotSpotModel();
        model.setId(entity.getCsvId());
        model.setName(entity.getSiteName());
        model.setAddress(entity.getAddress());
        model.setType(entity.getTourType());
        model.setDescription(entity.getDescription());
        model.setPictureURL(entity.getImageUrl());
        
        // Parse geometry from JSON or use lat/lon directly
        if (entity.getLatitude() != null && entity.getLongitude() != null) {
            model.setLocation(MultiPointModel.builder()
                    .x(entity.getLongitude())
                    .y(entity.getLatitude())
                    .type("MultiPoint")
                    .build());
        } else if (entity.getGeometry() != null) {
            model.setLocation(parseGeometry(entity.getGeometry()));
        }
        
        return model;
    }
    
    /**
     * Parse geometry JSON (fallback for records without parsed lat/lon)
     */
    private MultiPointModel parseGeometry(String geometryJson) {
        try {
            JsonNode root = objectMapper.readTree(geometryJson);
            String type = root.has("type") ? root.get("type").asText() : "Unknown";
            JsonNode coordinates = root.get("coordinates");
            
            if (coordinates != null && coordinates.isArray() && coordinates.size() > 0) {
                JsonNode firstPoint = coordinates.get(0);
                if (firstPoint.isArray() && firstPoint.size() >= 2) {
                    return MultiPointModel.builder()
                            .x(firstPoint.get(0).asDouble())
                            .y(firstPoint.get(1).asDouble())
                            .type(type)
                            .build();
                }
            }
        } catch (Exception e) {
            log.warn("Error parsing geometry: {}", e.getMessage());
        }
        
        return MultiPointModel.builder().x(0.0).y(0.0).type("Unknown").build();
    }
}
