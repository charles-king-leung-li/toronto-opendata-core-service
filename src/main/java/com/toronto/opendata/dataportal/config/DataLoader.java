package com.toronto.opendata.dataportal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.toronto.opendata.dataportal.model.CulturalHotSpot;
import com.toronto.opendata.dataportal.repository.CulturalHotSpotRepository;

@Configuration
public class DataLoader {
    
    @SuppressWarnings("null")
    @Bean
    CommandLineRunner loadData(CulturalHotSpotRepository repository) {
        return args -> {
            // Only load sample data if database is empty
            if (repository.count() == 0) {
                repository.save(CulturalHotSpot.builder()
                    .name("Art Gallery of Ontario")
                    .description("One of the largest art museums in North America")
                    .category("Museum")
                    .type("Art Gallery")
                    .address("317 Dundas St W, Toronto")
                    .latitude(43.6536)
                    .longitude(-79.3925)
                    .website("https://ago.ca")
                    .build());
                
                repository.save(CulturalHotSpot.builder()
                    .name("Royal Ontario Museum")
                    .description("Canada's largest museum of world cultures and natural history")
                    .category("Museum")
                    .type("Museum")
                    .address("100 Queen's Park, Toronto")
                    .latitude(43.6677)
                    .longitude(-79.3948)
                    .website("https://rom.on.ca")
                    .build());
                
                repository.save(CulturalHotSpot.builder()
                    .name("Harbourfront Centre")
                    .description("Premier arts and culture destination on Toronto's waterfront")
                    .category("Cultural Centre")
                    .type("Arts Venue")
                    .address("235 Queens Quay W, Toronto")
                    .latitude(43.6385)
                    .longitude(-79.3817)
                    .website("https://harbourfrontcentre.com")
                    .build());
                
                System.out.println("Sample cultural hotspot data loaded!");
            }
        };
    }
}
