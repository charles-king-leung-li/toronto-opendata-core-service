package com.toronto.opendata.core.service;

import com.toronto.opendata.core.model.CkanPackageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CkanService {
    private static final String BASE_URL = "https://ckan0.cf.opendata.inter.prod-toronto.ca";
    private final RestTemplate restTemplate;

    public CkanService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CkanPackageResponse getPackageInfo(String packageId) {
        String url = UriComponentsBuilder
            .newInstance()
            .scheme("https")
            .host(BASE_URL.replace("https://", ""))
            .path("/api/3/action/package_show")
            .queryParam("id", packageId)
            .build()
            .toUriString();

        return restTemplate.getForObject(url, CkanPackageResponse.class);
    }

    public CkanPackageResponse getResourceInfo(String resourceId) {
        String url = UriComponentsBuilder
            .newInstance()
            .scheme("https")
            .host(BASE_URL.replace("https://", ""))
            .path("/api/3/action/resource_show")
            .queryParam("id", resourceId)
            .build()
            .toUriString();

        return restTemplate.getForObject(url, CkanPackageResponse.class);
    }
}
