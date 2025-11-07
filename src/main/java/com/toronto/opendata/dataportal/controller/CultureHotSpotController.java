package com.toronto.opendata.dataportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toronto.opendata.dataportal.dto.ApiResponse;
import com.toronto.opendata.dataportal.model.CulturalHotSpotModel;
import com.toronto.opendata.dataportal.service.CulturalHotSpotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/cultural-hotspots")
@Tag(name = "Cultural Hotspots", description = "Cultural Hotspots from CSV data")
public class CultureHotSpotController {
    
    private final CulturalHotSpotService culturalHotSpotService;
    
    @Autowired
    public CultureHotSpotController(CulturalHotSpotService culturalHotSpotService) {
        this.culturalHotSpotService = culturalHotSpotService;
    }

    @Operation(
        summary = "Get Cultural Hotspots from CSV",
        description = "Returns cultural hotspot points of interest from Toronto Open Data CSV file"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CulturalHotSpotModel>>> getCulturalHotSpots() {
        List<CulturalHotSpotModel> models = culturalHotSpotService.getCulturalHotSpots();
        return ResponseEntity.ok(ApiResponse.success(models, "Cultural hotspots retrieved from CSV successfully"));
    }

    @Operation(
        summary = "Get Cultural Hotspot with a given Id",
        description = "Returns cultural hotspot points of interest by Id from Toronto Open Data CSV file"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CulturalHotSpotModel>> getCulturalHotSpotById(@PathVariable String id) {
        CulturalHotSpotModel model = culturalHotSpotService.getCulturalHotSpotById(id);
        if (model != null) {
            return ResponseEntity.ok(ApiResponse.success(model, "Cultural hotspot retrieved successfully"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("Cultural hotspot not found"));
        }
    }
}