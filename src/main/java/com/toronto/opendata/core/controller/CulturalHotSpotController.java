package com.toronto.opendata.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toronto.opendata.core.model.CulturalHotSpotModel;
import com.toronto.opendata.core.service.CulturalHotSpotService;

@RestController
@RequestMapping("/api/cultural-hotspots")
public class CulturalHotSpotController {
    
    private final CulturalHotSpotService service;
    
    @Autowired
    public CulturalHotSpotController(CulturalHotSpotService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<CulturalHotSpotModel>> getAllHotSpots() {
        return ResponseEntity.ok(service.getAllCulturalHotSpots());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CulturalHotSpotModel> getHotSpotById(@PathVariable String id) {
        CulturalHotSpotModel hotSpot = service.getCulturalHotSpotById(id);
        if (hotSpot != null) {
            return ResponseEntity.ok(hotSpot);
        }
        return ResponseEntity.notFound().build();
    }
}
