package com.toronto.opendata.core.service;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class CSVReaderService {
    
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();
    
    /**
     * Reads a CSV file and returns its contents as a list of records
     * @param filePath Path to the CSV file (classpath resource path)
     * @return List of CSV records with header names as map keys
     * @throws IOException if file cannot be read
     */
    public List<Map<String, String>> readCSV(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath must not be null");
        }
        List<Map<String, String>> records = new ArrayList<>();
        
        // Load from classpath
        ClassPathResource resource = new ClassPathResource(filePath);
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSV_FORMAT)) {
                 
            for (CSVRecord record : csvParser) {
                records.add(record.toMap());
            }
        }
        
        return records;
    }
    
    /**
     * Validates if a file exists and is a CSV file
     * @param filePath Path to the file to validate
     * @return true if file exists and has .csv extension
     */
    public boolean isValidCSVFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        Path path = Path.of(filePath);
        return path.toFile().exists() && 
               path.toFile().isFile() && 
               filePath.toLowerCase().endsWith(".csv");
    }
    
    /**
     * Reads the header row of a CSV file
     * @param filePath Path to the CSV file
     * @return List of header names
     * @throws IOException if file cannot be read
     */
    public List<String> readCSVHeaders(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSV_FORMAT)) {
            return new ArrayList<>(csvParser.getHeaderNames());
        }
    }
    
    /**
     * Streams CSV records one by one, useful for large files
     * @param filePath Path to the CSV file
     * @return Stream of CSV records
     * @throws IOException if file cannot be read
     */
    public Stream<CSVRecord> streamCSV(String filePath) throws IOException {
        Reader reader = new FileReader(filePath);
        CSVParser parser = new CSVParser(reader, CSV_FORMAT);
        return StreamSupport.stream(parser.spliterator(), false)
                .onClose(() -> {
                    try {
                        parser.close();
                        reader.close();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
    
    /**
     * Creates a StreamingResponseBody for a CSV file
     * @param filePath Path to the CSV file
     * @return StreamingResponseBody that can be used in a REST controller
     */
    public StreamingResponseBody streamCSVToResponse(String filePath) {
        return outputStream -> {
            try (Reader reader = new FileReader(filePath);
                 CSVParser parser = new CSVParser(reader, CSV_FORMAT);
                 Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                
                // Write headers
                writer.write(String.join(",", parser.getHeaderNames()) + "\n");
                
                // Stream each record
                for (CSVRecord record : parser) {
                    writer.write(String.join(",", record.values()) + "\n");
                    writer.flush();
                }
            }
        };
    }
}
