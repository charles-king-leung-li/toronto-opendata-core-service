package com.toronto.opendata.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI torontoOpenDataAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Toronto Open Data Core Service")
                        .description("Core business logic service for Toronto Open Data - handles data processing, geospatial calculations, and CSV operations")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("City of Toronto")
                                .url("https://www.toronto.ca/city-government/data-research-maps/open-data/"))
                        .license(new License()
                                .name("Open Government Licence – Toronto")
                                .url("https://open.toronto.ca/open-data-license/")));
    }
}
