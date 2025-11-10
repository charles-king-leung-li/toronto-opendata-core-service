package com.toronto.opendata.core.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * GeoJSON FeatureCollection format for map visualization
 * This is the standard format used by Leaflet, Mapbox, Google Maps, etc.
 */
@Data
@Builder
public class GeoJsonFeatureCollectionDTO {
    private String type;  // Always "FeatureCollection"
    private List<GeoJsonFeatureDTO> features;
}
