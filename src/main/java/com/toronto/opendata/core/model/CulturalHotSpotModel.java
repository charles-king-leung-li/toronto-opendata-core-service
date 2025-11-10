package com.toronto.opendata.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CulturalHotSpotModel {
    private String id;
    private String name;
    private String address;
    private String type;
    private String description;
    private String pictureURL;
    private MultiPointModel location;
}
