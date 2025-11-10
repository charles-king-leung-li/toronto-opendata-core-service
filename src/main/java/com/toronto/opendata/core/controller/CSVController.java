package com.toronto.opendata.core.controller;

import com.toronto.opendata.core.service.CSVReaderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
public class CSVController {

    private final CSVReaderService csvReaderService;

    public CSVController(CSVReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

    @GetMapping("/headers")
    public ResponseEntity<List<String>> getHeaders(@RequestParam String filePath) {
        try {
            if (!csvReaderService.isValidCSVFile(filePath)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(csvReaderService.readCSVHeaders(filePath));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<Map<String, String>>> readCSV(@RequestParam String filePath) {
        try {
            if (!csvReaderService.isValidCSVFile(filePath)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(csvReaderService.readCSV(filePath));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<StreamingResponseBody> streamCSV(
            @RequestParam String filePath,
            @RequestParam(required = false) String downloadFilename) {
        
        if (!csvReaderService.isValidCSVFile(filePath)) {
            return ResponseEntity.badRequest().build();
        }

        String filename = downloadFilename != null ? downloadFilename : "download.csv";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(csvReaderService.streamCSVToResponse(filePath));
    }
}
