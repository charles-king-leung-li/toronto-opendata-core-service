# Toronto Open Data - Core Service

## Overview
This is the **Business Logic and Data Service** layer of the Toronto Open Data microservices architecture. It handles all business logic, data processing, and persistence.

## Architecture
```
API Gateway (Port 8080) → Core Service (Port 8081) → Database/CSV Files
```

## Tech Stack
- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- H2 Database (in-memory)
- Apache Commons CSV
- Lombok

## Running the Application

### Prerequisites
- Java 17
- Maven 3.9+

### Start the Server
```bash
./mvnw spring-boot:run
```

The Core Service will start on **port 8081**.

## API Endpoints

### Cultural Hotspots
- `GET /api/cultural-hotspots` - Get all cultural hotspots from CSV
- `GET /api/cultural-hotspots/{id}` - Get hotspot by ID

## Configuration

### application.properties
```properties
server.port=8081
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

### H2 Console
Access at: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## Project Structure
```
src/main/java/com/toronto/opendata/core/
├── CoreServiceApplication.java      # Main application
├── controller/
│   └── CulturalHotSpotController.java  # REST endpoints
├── service/
│   └── CulturalHotSpotService.java     # Business logic
├── model/
│   ├── CulturalHotSpotModel.java       # Domain models
│   └── MultiPointModel.java
└── repository/                          # Data access (future)

src/main/resources/
└── data/
    └── points-of-interest-05-11-2025.csv  # Cultural hotspots data
```

## Responsibilities
- Execute business logic
- Manage data persistence
- Process CSV files
- Expose REST APIs for API Gateway
- Handle data transformations

## Data Sources
Currently using CSV files from `src/main/resources/data/`:
- `points-of-interest-05-11-2025.csv` - Cultural hotspots data

## Future Enhancements
- [ ] Migrate CSV data to database
- [ ] Implement pagination
- [ ] Add search functionality
- [ ] Implement caching
- [ ] Add data validation
- [ ] Support multiple data sources (CKAN API integration)

## Development Notes

### CSV Data Format
The cultural hotspots CSV contains:
- `_id` - Unique identifier
- `SiteName` - Name of the location
- `Address` - Street address
- `TourType` - Category/type
- `Description` - Details about the location
- `ImageURL` - Picture URL
- `geometry` - GeoJSON coordinates

### Geometry Parsing
Coordinates are stored as GeoJSON MultiPoint format:
```json
{
  "coordinates": [[-79.48576, 43.685]],
  "type": "MultiPoint"
}
```

## Related Projects
- [API Gateway](../toronto-opendata-api-gateway/) - BFF layer for frontend
- [Original Monolith](../toronto-opendata-api/) - Original monolithic application
