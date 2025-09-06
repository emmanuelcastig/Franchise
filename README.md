# Franchise Service

Franchise Service is a reactive Spring Boot application built with a **hexagonal (clean) architecture**.  
It manages franchises, branches, and products, and exposes REST APIs with **functional routing**.  
The project is fully containerized with **Docker** and supports both **local MongoDB** (via Docker Compose) and **MongoDB Atlas** in the cloud.

---

## Project Structure

The solution follows the [Bancolombia Hexagonal Architecture Scaffold](https://github.com/bancolombia/scaffold-clean-architecture):


- **`api`** → Defines the HTTP endpoints using functional `RouterFunction`.
- **`usecase`** → Contains application business logic (e.g. max stock per branch).
- **`model`** → Core domain objects (pure business entities).
- **`mongo`** → MongoDB implementation of repositories.
- **`deployment`** → Infrastructure configuration for containerization.

---

##  Prerequisites

- **Java 21**
- **Gradle** (wrapper included)
- **Docker & Docker Compose**
- (Optional) **MongoDB Compass** to explore data

---

##  Running Locally (with Docker Compose)

Clone the repository and from the root run:

```bash
# Make sure you created the .env file first!
docker-compose up --build
