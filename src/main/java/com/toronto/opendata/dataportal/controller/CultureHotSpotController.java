package com.toronto.opendata.dataportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toronto.opendata.dataportal.dto.ApiResponse;
import com.toronto.opendata.dataportal.model.CulturalHotSpot;
import com.toronto.opendata.dataportal.model.CulturalHotSpotModel;
import com.toronto.opendata.dataportal.service.CulturalHotSpotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/cultural-hotspots")
@Tag(name = "Cultural Hotspots", description = "Cultural Hotspots API - Currently using CSV data")
public class CultureHotSpotController {
    
    private final CulturalHotSpotService culturalHotSpotService;
    
    @Autowired
    public CultureHotSpotController(CulturalHotSpotService culturalHotSpotService) {
        this.culturalHotSpotService = culturalHotSpotService;
    }

    @Operation(
        summary = "Get All Cultural Hotspots from CSV",
        description = "Returns all cultural hotspot points of interest from Toronto Open Data CSV file. " +
                      "Note: This loads all data at once. Pagination will be available when database is implemented."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CulturalHotSpotModel>>> getCulturalHotSpots() {
        List<CulturalHotSpotModel> models = culturalHotSpotService.getCulturalHotSpots();
        return ResponseEntity.ok(ApiResponse.success(models, "Retrieved " + models.size() + " cultural hotspots from CSV"));
    }

    @Operation(
        summary = "Get Cultural Hotspot by ID from CSV",
        description = "Returns a specific cultural hotspot by its ID from the CSV file"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CulturalHotSpotModel>> getCulturalHotSpotById(
            @Parameter(description = "ID of the cultural hotspot") @PathVariable String id) {
        CulturalHotSpotModel model = culturalHotSpotService.getCulturalHotSpotById(id);
        if (model != null) {
            return ResponseEntity.ok(ApiResponse.success(model, "Cultural hotspot retrieved successfully"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("Cultural hotspot not found with id: " + id));
        }
    }
    
    // ========== Database Endpoints (Future Implementation with Pagination) ==========
    
    /**
     * TODO: Implement when database is ready
     * This endpoint will return paginated results from the database
     * 
     * Example usage:
     * - GET /api/cultural-hotspots/db?page=0&size=20&sort=name
     * - GET /api/cultural-hotspots/db?page=1&size=50&sort=name,desc
     */
    @Operation(
        summary = "[TODO] Get Cultural Hotspots from Database with Pagination",
        description = "Returns paginated cultural hotspots from database. " +
                      "Use page (default: 0), size (default: 20), and sort parameters. " +
                      "Currently not implemented - use CSV endpoints."
    )
    @GetMapping("/db")
    public ResponseEntity<ApiResponse<Page<CulturalHotSpot>>> getAllHotSpotsFromDb(
            @Parameter(description = "Page number (0-indexed)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of items per page") 
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Sort field and direction (e.g., 'name' or 'name,desc')") 
            @RequestParam(defaultValue = "name") String sort) {
        
        // Parse sort parameter
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        // TODO: Uncomment when database is implemented
        // Page<CulturalHotSpot> hotspots = culturalHotSpotService.getAllHotSpotsFromDb(pageable);
        // return ResponseEntity.ok(ApiResponse.success(hotspots, "Retrieved page " + page + " of cultural hotspots"));
        
        return ResponseEntity.status(501).body(
            ApiResponse.error("Database implementation pending - use /api/cultural-hotspots for CSV data")
        );
    }
}