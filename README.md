
---

# 🎬 IMDb Clone — Movie & TV Series Platform

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0202?style=for-the-badge&logo=flyway&logoColor=white)

A modern, robust, and scalable full-stack clone of IMDb (Internet Movie Database). Built with clean architecture principles, featuring a rich domain model modeled after real-world IMDb datasets, strong type safety, and RESTful APIs.

---

## 🚀 Key Features

* **🎥 Title Management:** Support for Movies, TV Shows, and Episodes with hierarchical relationships.
* **🎭 Cast & Crew Filmography:** Many-to-many relationship linking actors, directors, writers, and producers with specific character roles.
* **🏷️ Rich Metadata:** Tagging and categorization through dynamic **Genres**, **Keywords**, and **Localized Titles (AKAs)**.
* **⭐ User Reviews & Ratings:** Unique rating submissions per user, automated average rating computation, and user watchlists.
* **🛡️ Production-Ready Backend:** Strict DTO layering, custom business exceptions, Spring Data JPA dirty checking, and Flyway-managed schema migrations.

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 17 / 21
* **Framework:** Spring Boot 4 (Spring Web, Spring Data JPA, Spring Validation)
* **Database:** PostgreSQL
* **Database Migrations:** Flyway
* **Utilities:** Project Lombok (Builder pattern, immutability)
* **Testing:** JUnit 5, Mockito, AssertJ

### Frontend (In Progress)
* **Framework:** Angular (Standalone Components, Signals, RxJS)
* **Styling:** Modern Responsive UI / Tailwind CSS

---

## 🗄️ Database Schema & Domain Model

The database design closely replicates the official IMDb data model:

```
[ Title ] <==== (1:N) ====> [ MovieCast ] <==== (N:1) ====> [ Person ]
|                              |
(M:N)                          (M:N)
|                              |
[ Genre ]                      [ Keyword ]
```

### Core Entities:
* **`Title`**: Represents movies, TV series, or episodes (`title_type`, `start_year`, `runtime_minutes`, etc.).
* **`Person`**: Represents industry professionals (`first_name`, `last_name`, `birth_date`, `death_date`).
* **`MovieCast`**: Rich join entity linking titles and persons with a specific `JobRole` and `character_name`.
* **`Genre` & `Keyword`**: Categorization metadata for titles.
* **`User` & `Review`**: Social layer for user ratings (1-10 scale) and textual reviews.

---

## 🔌 REST API Endpoints Overview

All endpoints follow strict REST conventions and return standardized JSON responses.

### Titles (`/api/v1/titles`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/titles` | Create a new movie / series | `201 Created` |
| `GET` | `/api/v1/titles` | Retrieve all titles | `200 OK` |
| `GET` | `/api/v1/titles/{id}` | Get title details by ID | `200 OK` |
| `PUT` | `/api/v1/titles/{id}` | Full update of a title | `200 OK` |
| `PATCH` | `/api/v1/titles/{id}` | Partial update of specific title fields | `200 OK` |
| `DELETE` | `/api/v1/titles/{id}` | Remove a title | `204 No Content` |

### Cast & Crew (`/api/v1/casts`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/casts` | Assign an actor/crew member to a title | `201 Created` |
| `GET` | `/api/v1/casts/title/{titleId}` | Get full cast list for a given title | `200 OK` |
| `GET` | `/api/v1/casts/person/{personId}` | Get complete filmography for a person | `200 OK` |
| `PATCH` | `/api/v1/casts/{id}` | Update character name or job role | `200 OK` |
| `DELETE` | `/api/v1/casts/{id}` | Remove cast member association | `204 No Content` |

### Genres (`/api/v1/genres`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/genres` | Fetch all available movie genres | `200 OK` |

---

## 🧪 Testing

The codebase includes unit and integration tests written with **JUnit 5**, **Mockito**, and **AssertJ**.

To execute all tests:
```bash
./mvnw test
```

Test coverage focuses on:
* **Service Layer Happy Paths**: Verification of business logic, DTO mapping, and data persistence.
* **Edge Cases & Validation**: Duplicate association prevention, handling non-existent parent entities (`TitleNotFoundException`, `PersonNotFoundException`).
* **JPA Dirty Checking**: Ensuring updates are applied seamlessly without redundant repository calls.

---

## ⚙️ Getting Started

### Prerequisites
* **JDK 17** or higher
* **PostgreSQL 14+**
* **Maven 3.8+** (or use the provided `./mvnw` wrapper)

### 1. Clone the repository
```bash
git clone https://github.com/your-username/imdb-clone.git
cd imdb-clone
```

### 2. Configure Database
Ensure PostgreSQL is running, then create a database:
```sql
CREATE DATABASE imdb_db;
```

Update your `src/main/resources/application.yml` (or `application.properties`) with your database credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/imdb_db
    username: your_postgres_user
    password: your_postgres_password
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```
The application will automatically apply Flyway migrations and start on `http://localhost:8080`.

---

## 🗺️ Architectural Roadmap

This project is built iteratively following a **Modular Monolith to Polyglot Microservices** strategy:

- [x] **Phase 1: Core Domain & REST API (Spring Boot + Postgres)**
- [ ] **Phase 2: Database Migrations & Automated Seeding (Flyway + TMDB API)**
- [ ] **Phase 3: Security Layer (Spring Security + JWT Authentication)**
- [ ] **Phase 4: SPA Client (Angular 17+ with Signals & SSR)**
- [ ] **Phase 5: Polyglot Microservices Architecture**:
    - 🍃 **Java/Spring Cloud**: API Gateway & Core Business Services.
    - 🐍 **Python (FastAPI + Scikit-Learn)**: Recommendation Engine based on user watchlists & review sentiment analysis.
    - 🐹 **Go (Golang)**: Ultra-fast autocomplete search & real-time WebSocket notifications.
    - 📨 **Apache Kafka**: Event-driven asynchronous communication between microservices.
    - 🐳 **Docker Compose**: Containerized multi-service deployment.

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
