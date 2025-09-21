# ⚡ Charging Station Service

A Spring Boot 3.5 application for managing and filtering charging stations. Built with **Java 17**, **Spring Boot**, **Spring Data JPA**, **PostgreSQL**, and **Testcontainers**.

---

## Features
- Add, update, delete, and fetch charging stations
- Filter stations by:
    - Name
    - Power
    - Zipcode
    - Coordinates (latitude, longitude, distance)
- REST API endpoints with DTOs
- Unit tests with Mockito

---

## Tech Stack
- Java 17
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Lombok
- Liquibase (DB migrations)
- Testcontainers
- JUnit 5, Mockito

---

## Project Structure
com.hubject.chargingstation

- controller # REST controllers
- dto # Request and Response DTOs
- entity # JPA entities
- filter # Filtering predicates and logic
- repository # Spring Data JPA repositories
- service # Business logic and SpecificationResolver
- Test

  

The application will run on http://localhost:8083.

## API Endpoints
GET /api/charging-stations – get all stations

GET /api/charging-stations/{id} – get by ID

POST /api/charging-stations – create station

PUT /api/charging-stations/{id} – update station

DELETE /api/charging-stations/{id} – delete station

GET /api/charging-stations – get all stations, can filter by coordinate and distance (coordinate + distanceKm)

## Filtering Example

To filter by coordinates and distance:  
`GET /api/charging-stations?latitude=...&longitude=...&distanceKm=...`
