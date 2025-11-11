package com.toronto.opendata.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toronto.opendata.core.entity.CulturalHotSpotEntity;
import com.toronto.opendata.core.repository.CulturalHotSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMigrationService {
    
    private final CulturalHotSpotRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String CSV_FILE_PATH = "data/points-of-interest-05-11-2025.csv";
    
    /**
     * Automatically migrate CSV data to PostgreSQL on application startup
     * Only runs if database is empty
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void migrateDataOnStartup() {
        try {
            long count = repository.count();
            if (count == 0) {
                log.info("Database is empty. Starting automatic CSV migration...");
                migrateCsvToDatabase();
            } else {
                log.info("Database already contains {} records. Skipping migration.", count);
            }
        } catch (Exception e) {
            log.error("Error during startup migration check", e);
        }
    }
    
    /**
     * Migrate all CSV data to PostgreSQL database
     */
    @Transactional
    public void migrateCsvToDatabase() {
        try {
            log.info("Starting CSV to PostgreSQL migration...");
            
            ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );
            
            CSVParser csvParser = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
            
            int recordCount = 0;
            int successCount = 0;
            int errorCount = 0;
            
            for (CSVRecord record : csvParser) {
                recordCount++;
                try {
                    CulturalHotSpotEntity entity = mapCsvRecordToEntity(record);
                    repository.save(entity);
                    successCount++;
                    
                    if (successCount % 50 == 0) {
                        log.info("Migrated {} records...", successCount);
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Error migrating record {}: {}", recordCount, e.getMessage());
                }
            }
            
            log.info("Migration completed! Total: {}, Success: {}, Errors: {}", 
                     recordCount, successCount, errorCount);
            
            csvParser.close();
            reader.close();
            
        } catch (Exception e) {
            log.error("Fatal error during CSV migration", e);
            throw new RuntimeException("CSV migration failed", e);
        }
    }
    
    /**
     * Map a CSV record to a database entity
     */
    private CulturalHotSpotEntity mapCsvRecordToEntity(CSVRecord record) throws Exception {
        CulturalHotSpotEntity entity = new CulturalHotSpotEntity();
        
        entity.setCsvId(getField(record, "_id"));
        entity.setLoopsGuide(getField(record, "LoopsGuide"));
        entity.setLoop(getField(record, "Loop"));
        entity.setTourNum(getField(record, "TourNum"));
        entity.setOrderNum(getField(record, "OrderNum"));
        entity.setLoopTourName(getField(record, "LoopTourName"));
        entity.setLoopTourUrl(getField(record, "LoopTourURL"));
        entity.setTourLabel(getField(record, "TourLabel"));
        entity.setSiteName(getField(record, "SiteName"));
        entity.setNeighbourhood(getField(record, "Neighbourhood"));
        entity.setTourType(getField(record, "TourType"));
        entity.setDuration(getField(record, "Duration"));
        entity.setEntryType(getField(record, "EntryType"));
        entity.setInterests(getField(record, "Interests"));
        entity.setDirectionsTransit(getField(record, "DirectionsTransit"));
        entity.setDirectionsCar(getField(record, "DirectionsCar"));
        entity.setDescription(getField(record, "Description"));
        entity.setAddress(getField(record, "Address"));
        entity.setExternalLink(getField(record, "ExternalLink"));
        entity.setImageCredit(getField(record, "ImageCredit"));
        entity.setImageCreditExternalLink(getField(record, "ImageCreditExternalLink"));
        entity.setImageAltText(getField(record, "ImageAltText"));
        entity.setImageUrl(getField(record, "ImageURL"));
        entity.setThumbUrl(getField(record, "ThumbURL"));
        entity.setImageOrientation(getField(record, "ImageOrientation"));
        entity.setTest1(getField(record, "test1"));
        entity.setTest2(getField(record, "test2"));
        entity.setTest3(getField(record, "test3"));
        entity.setObjectId(getField(record, "ObjectId"));
        
        // Parse geometry
        String geometryJson = getField(record, "geometry");
        entity.setGeometry(geometryJson);
        
        // Extract coordinates from geometry JSON
        if (geometryJson != null && !geometryJson.equals("None")) {
            try {
                JsonNode geometryNode = objectMapper.readTree(geometryJson);
                JsonNode coordinates = geometryNode.get("coordinates");
                if (coordinates != null && coordinates.isArray() && coordinates.size() > 0) {
                    JsonNode firstPoint = coordinates.get(0);
                    if (firstPoint.isArray() && firstPoint.size() >= 2) {
                        entity.setLongitude(firstPoint.get(0).asDouble());
                        entity.setLatitude(firstPoint.get(1).asDouble());
                    }
                }
            } catch (Exception e) {
                log.warn("Could not parse geometry for record {}: {}", entity.getCsvId(), e.getMessage());
            }
        }
        
        return entity;
    }
    
    /**
     * Safely get a field from CSV record, returning null for "None" values
     */
    private String getField(CSVRecord record, String fieldName) {
        try {
            String value = record.get(fieldName);
            return (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("None")) 
                ? null 
                : value.trim();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Clear all data from database (for re-migration)
     */
    @Transactional
    public void clearDatabase() {
        log.info("Clearing all data from database...");
        repository.deleteAll();
        log.info("Database cleared successfully");
    }
}
