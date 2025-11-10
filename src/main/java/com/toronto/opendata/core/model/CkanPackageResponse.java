package com.toronto.opendata.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CkanPackageResponse {
    private boolean success;
    private CkanResult result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CkanResult {
        private String id;
        private String name;
        private List<CkanResource> resources;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CkanResource {
        private String id;
        private String name;
        private String url;
        private boolean datastore_active;
        private String format;
    }
}
