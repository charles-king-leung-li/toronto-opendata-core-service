package com.toronto.opendata.dataportal.service;

import com.toronto.opendata.dataportal.dto.GeoJson.GeoJsonFeatureCollectionDTO;
import com.toronto.opendata.dataportal.dto.GeoJson.GeoJsonFeatureDTO;
import com.toronto.opendata.dataportal.dto.GeoJson.MapPointDTO;
import com.toronto.opendata.dataportal.model.CulturalHotSpotModel;
import com.toronto.opendata.dataportal.model.MultiPointModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapService {
    
    private final CulturalHotSpotService culturalHotSpotService;
    
    public MapService(CulturalHotSpotService culturalHotSpotService) {
        this.culturalHotSpotService = culturalHotSpotService;
    }
    
    /**
     * Get all cultural hotspots as simple map points
     */
    public List<MapPointDTO> getMapPoints() {
        return culturalHotSpotService.getCulturalHotSpots().stream()
                .filter(spot -> spot.getLocation() != null)
                .map(this::toMapPoint)
                .collect(Collectors.toList());
    }
    
    /**
     * Get cultural hotspots within a bounding box
     */
    public List<MapPointDTO> getMapPointsInBounds(Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return culturalHotSpotService.getCulturalHotSpots().stream()
                .filter(spot -> {
                    if (spot.getLocation() == null) return false;
                    double lat = spot.getLocation().getY();
                    double lon = spot.getLocation().getX();
                    return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
                })
                .map(this::toMapPoint)
                .collect(Collectors.toList());
    }
    
    /**
     * Get cultural hotspots as GeoJSON FeatureCollection
     * This format is compatible with Leaflet, Mapbox, and other mapping libraries
     */
    public GeoJsonFeatureCollectionDTO getGeoJsonFeatureCollection() {
        List<GeoJsonFeatureDTO> features = culturalHotSpotService.getCulturalHotSpots().stream()
                .filter(spot -> spot.getLocation() != null)
                .map(this::toGeoJsonFeature)
                .collect(Collectors.toList());
        
        return GeoJsonFeatureCollectionDTO.builder()
                .type("FeatureCollection")
                .features(features)
                .build();
    }
    
    /**
     * Get cultural hotspots within radius of a point
     */
    public List<MapPointDTO> getMapPointsNearby(Double centerLat, Double centerLon, Double radiusKm) {
        return culturalHotSpotService.getCulturalHotSpots().stream()
                .filter(spot -> {
                    if (spot.getLocation() == null) return false;
                    double distance = calculateDistance(
                            centerLat, centerLon,
                            spot.getLocation().getY(),
                            spot.getLocation().getX()
                    );
                    return distance <= radiusKm;
                })
                .map(this::toMapPoint)
                .collect(Collectors.toList());
    }
    
    private MapPointDTO toMapPoint(CulturalHotSpotModel model) {
        MultiPointModel location = model.getLocation();
        return MapPointDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .address(model.getAddress())
                .type(model.getType())
                .latitude(location.getY())
                .longitude(location.getX())
                .imageUrl(model.getPictureURL())
                .build();
    }
    
    private GeoJsonFeatureDTO toGeoJsonFeature(CulturalHotSpotModel model) {
        MultiPointModel location = model.getLocation();
        
        // GeoJSON coordinates are [longitude, latitude] (x, y)
        double[] coordinates = new double[]{location.getX(), location.getY()};
        
        GeoJsonFeatureDTO.GeometryDTO geometry = GeoJsonFeatureDTO.GeometryDTO.builder()
                .type("Point")
                .coordinates(coordinates)
                .build();
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", model.getId());
        properties.put("name", model.getName());
        properties.put("description", model.getDescription());
        properties.put("address", model.getAddress());
        properties.put("type", model.getType());
        properties.put("imageUrl", model.getPictureURL());
        
        return GeoJsonFeatureDTO.builder()
                .type("Feature")
                .geometry(geometry)
                .properties(properties)
                .build();
    }
    
    /**
     * Calculate distance between two points using Haversine formula
     * Returns distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
}
