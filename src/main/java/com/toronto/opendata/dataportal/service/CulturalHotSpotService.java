package com.toronto.opendata.dataportal.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toronto.opendata.dataportal.model.CulturalHotSpotModel;
import com.toronto.opendata.dataportal.model.MultiPointModel;
import com.toronto.opendata.dataportal.util.GeometryParser;

@Service
public class CulturalHotSpotService {

    private CSVReaderService csvReaderService;
    private String csvFilePath = "data/points-of-interest-05-11-2025.csv";
    
    @Autowired
    public CulturalHotSpotService(CSVReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

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

}