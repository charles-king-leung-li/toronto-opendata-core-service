package com.toronto.opendata.core.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for map point data with coordinates
 */
@Data
@Builder
public class MapPointDTO {
    private String id;
    private String name;
    private String description;
    private String address;
    private String type;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
}
