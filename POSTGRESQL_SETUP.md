# PostgreSQL Setup and Migration Guide

## Prerequisites

### Install PostgreSQL
1. Download PostgreSQL from: https://www.postgresql.org/download/windows/
2. Run the installer and remember your password for the `postgres` user
3. Default port is `5432`

### Quick Install with Chocolatey (Optional)
```powershell
choco install postgresql
```

## Database Setup

### Option 1: Using pgAdmin (GUI)
1. Open pgAdmin (comes with PostgreSQL installation)
2. Connect to PostgreSQL server (localhost)
3. Right-click on "Databases" → Create → Database
4. Name: `toronto_opendata`
5. Click Save

### Option 2: Using Command Line (psql)
```powershell
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE toronto_opendata;

# Exit
\q
```

## Configuration

The application is already configured to connect to PostgreSQL:

**File:** `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/toronto_opendata
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**Update the password** if you set a different one during PostgreSQL installation.

## Running the Migration

### Automatic Migration
The migration runs automatically when you start the application:
1. Application detects empty database
2. Reads CSV file from `resources/data/points-of-interest-05-11-2025.csv`
3. Creates `cultural_hotspots` table
4. Migrates all 902 records
5. Logs progress every 50 records

### Build and Run
```powershell
# Build the project
.\mvnw clean install

# Run the application
.\mvnw spring-boot:run
```

Watch the console for migration logs:
```
INFO - Database is empty. Starting automatic CSV migration...
INFO - Starting CSV to PostgreSQL migration...
INFO - Migrated 50 records...
INFO - Migrated 100 records...
...
INFO - Migration completed! Total: 902, Success: 902, Errors: 0
```

## Verify Migration

### Check Database
```sql
-- Connect to database
psql -U postgres -d toronto_opendata

-- Count records
SELECT COUNT(*) FROM cultural_hotspots;

-- View sample records
SELECT csv_id, site_name, neighbourhood, latitude, longitude 
FROM cultural_hotspots 
LIMIT 10;

-- Check neighbourhoods
SELECT DISTINCT neighbourhood 
FROM cultural_hotspots 
ORDER BY neighbourhood;
```

### Test API Endpoints
```powershell
# Get all hotspots
curl http://localhost:8081/api/cultural-hotspots

# Get hotspot by ID
curl http://localhost:8081/api/cultural-hotspots/1

# Search by neighbourhood
curl http://localhost:8081/api/cultural-hotspots?neighbourhood=Weston
```

## Database Schema

### Table: `cultural_hotspots`
- **id** - Primary key (auto-increment)
- **csv_id** - Original CSV ID
- **site_name** - Name of the location
- **neighbourhood** - Neighbourhood name
- **description** - Full description
- **address** - Street address
- **latitude/longitude** - Coordinates
- **geometry** - Original GeoJSON geometry
- **image_url** - Picture URL
- **tour_type** - Type of tour
- **interests** - Categories (Art, History, etc.)
- And 20+ more fields from CSV

### Indexes (Auto-created by JPA)
- Primary key on `id`
- Can add custom indexes later for performance

## Manual Migration Controls

If you need to re-run the migration:

1. **Clear database:**
```java
@Autowired
DataMigrationService migrationService;

// In a controller or startup code
migrationService.clearDatabase();
migrationService.migrateCsvToDatabase();
```

2. **Or drop and recreate:**
```sql
DROP DATABASE toronto_opendata;
CREATE DATABASE toronto_opendata;
```

Then restart the application.

## Troubleshooting

### Connection Refused
- Ensure PostgreSQL service is running
- Check Windows Services → postgresql-x64-[version]
- Or run: `pg_ctl status`

### Authentication Failed
- Update `application.properties` with correct password
- Or reset postgres password:
```powershell
psql -U postgres
ALTER USER postgres WITH PASSWORD 'newpassword';
```

### Migration Errors
- Check console logs for specific CSV parsing errors
- Verify CSV file exists at `src/main/resources/data/points-of-interest-05-11-2025.csv`
- Check CSV encoding (should be UTF-8)

## Next Steps

### Performance Optimization
- Add indexes on frequently queried fields:
```sql
CREATE INDEX idx_neighbourhood ON cultural_hotspots(neighbourhood);
CREATE INDEX idx_tour_type ON cultural_hotspots(tour_type);
CREATE INDEX idx_coordinates ON cultural_hotspots(latitude, longitude);
```

### PostGIS for Advanced Geo Features
To use advanced spatial queries, install PostGIS extension:
```sql
CREATE EXTENSION postgis;
ALTER TABLE cultural_hotspots ADD COLUMN geom GEOMETRY(Point, 4326);
UPDATE cultural_hotspots SET geom = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326);
CREATE INDEX idx_geom ON cultural_hotspots USING GIST(geom);
```

## Benefits of PostgreSQL Migration

✅ **Performance**: Database queries faster than CSV parsing  
✅ **Scalability**: Can handle millions of records  
✅ **Filtering**: SQL queries more efficient than stream filtering  
✅ **Transactions**: ACID compliance for data integrity  
✅ **Relationships**: Can add foreign keys for related data  
✅ **Indexing**: Fast lookups on any field  
✅ **Geospatial**: PostGIS support for advanced location queries  
