package com.toronto.opendata.core.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

/**
 * GeoJSON Feature format for map visualization
 * Format: https://geojson.org/
 */
@Data
@Builder
public class GeoJsonFeatureDTO {
    private String type;  // Always "Feature"
    private GeometryDTO geometry;
    private Map<String, Object> properties;
    
    @Data
    @Builder
    public static class GeometryDTO {
        private String type;  // "Point", "MultiPoint", etc.
        private Object coordinates;  // [longitude, latitude] for Point
    }
}
