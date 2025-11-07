# Toronto Open Data API

Backend API service providing Toronto's open data to mobile and web applications.

## 🎯 Overview

This is a Spring Boot RESTful API that serves as the backend layer for Toronto Open Data applications. It provides structured access to Toronto's cultural hotspots, points of interest, and other civic datasets through a modern REST API.

### Architecture

```
┌─────────────────────┐
│  React Native App   │  ← Frontend (Mobile)
│   (Your Frontend)   │
└──────────┬──────────┘
           │
           │ REST API
           │
┌──────────▼──────────┐
│ Toronto Open Data   │  ← This Repository
│      API Layer      │
└──────────┬──────────┘
           │
           │ Data Sources
           │
┌──────────▼──────────┐
│  • CSV Files        │
│  • Database (H2)    │
│  • Future: Postgres │
└─────────────────────┘
```

## 🚀 Features

- **Cultural Hotspots API** - Access Toronto's cultural points of interest
- **Map Integration** - GeoJSON support for mapping libraries
- **CORS Enabled** - Ready for frontend consumption
- **Pagination Support** - Efficient data loading (TODO: Database implementation)
- **API Documentation** - Interactive Swagger UI
- **Environment-based Configuration** - Easy deployment across environments

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.9.11 or higher
- (Optional) Your favorite IDE (IntelliJ IDEA, VS Code, Eclipse)

## 🛠️ Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/charles-king-leung-li/dataserviceprovider.git
cd dataserviceprovider
```

### 2. Set Up Environment Variables

```bash
# Copy example files
cp .env.example .env
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties

# Add your API keys to these files (they're gitignored)
```

### 3. Run the Application

Using Maven Wrapper:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`

### 4. Access the API

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## 📡 API Endpoints

### Cultural Hotspots

#### Get All Hotspots (CSV)
```http
GET /api/cultural-hotspots
```

**Response:**
```json
{
  "data": [...],
  "message": "Retrieved 500 cultural hotspots from CSV",
  "status": "success"
}
```

#### Get Hotspot by ID
```http
GET /api/cultural-hotspots/{id}
```

#### Future: Database with Pagination (TODO)
```http
GET /api/cultural-hotspots/db?page=0&size=20&sort=name
```

### Map Integration

```http
GET /api/map/points                    # Get map points
GET /api/map/geojson                   # Get GeoJSON format
GET /api/map/nearby?lat=43.65&lon=-79.38&radius=5
```

## 🗄️ Data Sources

### Current (Temporary)
- **CSV Files**: Located in `src/main/resources/data/`
  - `points-of-interest-05-11-2025.csv` - Cultural hotspots
  - `Places of Worship - 4326.csv` - Places of worship
  - `tpl-branch-general-information-2023.json` - Library branches

### Future (Permanent)
- **PostgreSQL Database** - Will migrate CSV data to relational database
- **Pagination** - Full pagination support on database queries

## 🔧 Configuration

### Environment Variables

See `.env.example` for all available configuration options.

**Required:**
- `GOOGLE_MAPS_API_KEY` - For map integration features

**Optional (with defaults):**
- `CORS_ALLOWED_ORIGINS` - Comma-separated allowed origins
- `SERVER_PORT` - API port (default: 8080)

### Profiles

- **local** - Development with H2 database
- **prod** - Production configuration (TODO)

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 📦 Building for Production

```bash
# Build JAR
./mvnw clean package

# Run the JAR
java -jar target/toronto-opendata-api-0.0.1-SNAPSHOT.jar
```

## 🌐 CORS Configuration

CORS is configured for local development by default. For production:

```bash
# Set environment variable
export CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com
```

See [CORS Setup Documentation](documentation/CORS_SETUP.md) for details.

## 📚 Documentation

Detailed documentation is available in the `documentation/` folder:

- [CORS Setup](documentation/CORS_SETUP.md)
- [Environment Variables](documentation/ENV_SETUP.md)
- [Git Commit Checklist](documentation/GIT_COMMIT_CHECKLIST.md)
- [Cultural Hotspots API](documentation/CULTURAL_HOTSPOTS_API.md)
- [Map API](documentation/MAP_API.md)
- [Google Maps Setup](documentation/GOOGLE_MAPS_SETUP.md)

## 🏗️ Project Structure

```
toronto-opendata-api/
├── src/
│   ├── main/
│   │   ├── java/com/toronto/opendata/dataportal/
│   │   │   ├── controller/     # REST endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access
│   │   │   ├── model/          # Domain models
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── config/         # Configuration classes
│   │   │   └── util/           # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data/           # CSV datasets
│   │       └── static/         # Static resources
│   └── test/                   # Unit and integration tests
├── documentation/              # Project documentation
├── pom.xml                    # Maven dependencies
└── README.md                  # This file
```

## 🚧 Roadmap

- [x] CSV data loading
- [x] REST API endpoints
- [x] CORS configuration
- [x] API documentation (Swagger)
- [ ] Database migration (PostgreSQL)
- [ ] Pagination implementation
- [ ] Authentication/Authorization
- [ ] Rate limiting
- [ ] Caching layer (Redis)
- [ ] Docker deployment
- [ ] CI/CD pipeline

## 🤝 Contributing

This is a personal project, but suggestions and feedback are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project uses public data from [Toronto Open Data](https://open.toronto.ca/).

## 👤 Author

**Charles King Leung Li**
- GitHub: [@charles-king-leung-li](https://github.com/charles-king-leung-li)

## 🙏 Acknowledgments

- Data provided by [City of Toronto Open Data](https://open.toronto.ca/)
- Built with [Spring Boot](https://spring.io/projects/spring-boot)

---

**Note**: This is the backend API layer. For the frontend React Native application, see https://github.com/charles-king-leung-li/TorontoOpenDataReactFE.
