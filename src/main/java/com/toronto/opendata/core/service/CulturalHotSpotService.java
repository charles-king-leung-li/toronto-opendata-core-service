package com.toronto.opendata.core.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.toronto.opendata.core.model.CulturalHotSpotModel;
import com.toronto.opendata.core.model.MultiPointModel;

@Service
public class CulturalHotSpotService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String csvFilePath = "data/points-of-interest-05-11-2025.csv";
    
    public List<CulturalHotSpotModel> getAllCulturalHotSpots() {
        List<CulturalHotSpotModel> hotSpots = new ArrayList<>();
        
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            
            CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build());
            
            for (CSVRecord record : csvParser) {
                CulturalHotSpotModel hotSpot = new CulturalHotSpotModel();
                hotSpot.setId(record.get("_id"));
                hotSpot.setName(record.get("SiteName"));
                hotSpot.setAddress(record.get("Address"));
                hotSpot.setType(record.get("TourType"));
                hotSpot.setDescription(record.get("Description"));
                hotSpot.setPictureURL(record.get("ImageURL"));
                
                // Parse geometry
                String geometryJson = record.get("geometry");
                if (geometryJson != null && !geometryJson.trim().isEmpty()) {
                    hotSpot.setLocation(parseGeometry(geometryJson));
                }
                
                hotSpots.add(hotSpot);
            }
            
            csvParser.close();
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load cultural hotspots", e);
        }
        
        return hotSpots;
    }
    
    public CulturalHotSpotModel getCulturalHotSpotById(String id) {
        return getAllCulturalHotSpots().stream()
                .filter(spot -> spot.getId() != null && spot.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
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
            // Return default on error
        }
        
        return MultiPointModel.builder().x(0.0).y(0.0).type("Unknown").build();
    }
}
