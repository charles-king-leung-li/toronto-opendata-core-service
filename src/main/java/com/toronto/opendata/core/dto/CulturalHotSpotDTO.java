package com.toronto.opendata.core.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CulturalHotSpotDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String website;
    private String location;
    private Double latitude;
    private Double longitude;
}
