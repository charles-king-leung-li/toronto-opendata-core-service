package com.toronto.opendata.core.controller;

import com.toronto.opendata.core.dto.GeoJsonFeatureCollectionDTO;
import com.toronto.opendata.core.dto.MapPointDTO;
import com.toronto.opendata.core.service.MapService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {
    
    private final MapService mapService;
    
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }
    
    @GetMapping("/points")
    public List<MapPointDTO> getAllMapPoints() {
        return mapService.getMapPoints();
    }
    
    @GetMapping("/geojson")
    public GeoJsonFeatureCollectionDTO getGeoJson() {
        return mapService.getGeoJsonFeatureCollection();
    }
    
    @GetMapping("/points/bounds")
    public List<MapPointDTO> getMapPointsInBounds(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLon,
            @RequestParam Double maxLon
    ) {
        return mapService.getMapPointsInBounds(minLat, maxLat, minLon, maxLon);
    }
    
    @GetMapping("/points/nearby")
    public List<MapPointDTO> getNearbyMapPoints(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "5.0") Double radiusKm
    ) {
        return mapService.getMapPointsNearby(lat, lon, radiusKm);
    }
}
