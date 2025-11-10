package com.toronto.opendata.coreservice.transformer;

import com.toronto.opendata.coreservice.dto.CulturalHotSpotDTO;
import com.toronto.opendata.core.model.CulturalHotSpotModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CulturalHotSpotTransformer {
    
    public CulturalHotSpotDTO toDTO(CulturalHotSpotModel model) {
        // Extract latitude and longitude from location if available
        Double latitude = null;
        Double longitude = null;
        if (model.getLocation() != null) {
            latitude = model.getLocation().getY();  // y is latitude
            longitude = model.getLocation().getX(); // x is longitude
        }
        
        return CulturalHotSpotDTO.builder()
                .id(model.getId() != null ? Long.parseLong(model.getId()) : null)
                .name(model.getName())
                .description(model.getDescription())
                .category(model.getType())  // Using 'type' as category
                .website(model.getPictureURL())  // Using pictureURL as website for now
                .location(model.getAddress())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public List<CulturalHotSpotDTO> toDTOList(List<CulturalHotSpotModel> models) {
        return models.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
