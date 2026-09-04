# 🎬 IMDb Clone — Movie & TV Series Platform

![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)

A modern, robust, and scalable full-stack clone of IMDb (Internet Movie Database). Built with clean architecture principles, featuring a rich domain model modeled after real-world IMDb datasets, strict DTO layering, strong type safety, and RESTful APIs.

---

## 🚀 Key Features

* **🎥 Title Management:** Support for Movies, TV Shows, and Episodes with hierarchical relationships.
* **🎭 Cast & Crew Filmography:** Many-to-many relationship linking actors, directors, writers, and producers with specific character roles.
* **🏷️ Rich Metadata:** Tagging and categorization through dynamic **Genres**, **Keywords**, and **Localized Titles (AKAs)**.
* **⭐ User Reviews & Ratings:** Unique rating submissions per user, automated average rating computation, and user watchlists.
* **🛡️ Production-Ready Backend:** Strict DTO layering, custom business exceptions, Spring Data JPA dirty checking, and Swagger/OpenAPI documentation.

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 21+
* **Framework:** Spring Boot 4.x (Spring Web, Spring Data JPA, Spring Validation)
* **Database:** PostgreSQL 15 (Docker containerized)
* **Security & Auth:** Spring Security, JJWT (Stateless JWT Authentication)
* **Utilities:** Project Lombok (Builder pattern, immutability)
* **API Documentation:** SpringDoc OpenAPI / Swagger UI
* **Testing:** JUnit 5, Mockito, AssertJ

### Frontend (In Progress)
* **Framework:** Angular 17+ (Standalone Components, Signals, RxJS)
* **Styling:** Modern Responsive UI / Tailwind CSS

---

## 🗄️ Database Schema & Domain Model

The database design closely replicates the official IMDb data model:

```text
[ Title ] <==== (1:N) ====> [ MovieCast ] <==== (N:1) ====> [ Person ]
    |                              |
  (M:N)                          (M:N)
    |                              |
[ Genre ]                      [ Keyword ]
```

### Core Entities:
* **`Title`**: Represents movies, TV series, or episodes (`title_type`, `start_year`, `runtime_minutes`, etc.).
* **`Person`**: Represents industry professionals (`first_name`, `last_name`, `birth_date`, `death_date`, `role`).
* **`MovieCast`**: Rich join entity linking titles and persons with a specific `JobRole` and `character_name`.
* **`Genre` & `Keyword`**: Categorization metadata for titles.
* **`User` & `Review`**: Social layer for user ratings (1-10 scale) and textual reviews.
* **`TitleRating`**: Aggregated statistics (`average_rating`, `num_votes`) automatically calculated on review changes.
* **`Watchlist`**: User-curated list of titles to watch.

---

## 🔌 REST API Endpoints Overview

All endpoints follow strict REST conventions and return standardized JSON responses.

### Titles (`/api/titles`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/titles` | Create a new movie / series | `201 Created` |
| `GET` | `/api/titles` | Retrieve all titles | `200 OK` |
| `GET` | `/api/titles/{id}` | Get title details by ID (with rating, genres, cast) | `200 OK` |
| `PUT` | `/api/titles/{id}` | Full update of a title | `200 OK` |
| `PATCH` | `/api/titles/{id}` | Partial update of specific title fields | `200 OK` |
| `DELETE` | `/api/titles/{id}` | Remove a title | `204 No Content` |

### Cast & Crew (`/api/casts`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/casts` | Assign an actor/crew member to a title | `201 Created` |
| `GET` | `/api/casts/title/{titleId}` | Get full cast list for a given title | `200 OK` |
| `GET` | `/api/casts/person/{personId}` | Get complete filmography for a person | `200 OK` |
| `PATCH` | `/api/casts/{id}` | Update character name or job role | `200 OK` |
| `DELETE` | `/api/casts/{id}` | Remove cast member association | `204 No Content` |

### Genres & Keywords (`/api/genres`, `/api/keywords`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/genres` | Fetch all available movie genres | `200 OK` |
| `POST` | `/api/genres` | Create a new genre | `201 Created` |
| `GET` | `/api/keywords` | Fetch all movie keywords | `200 OK` |

### Reviews (`/api/reviews`)
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/reviews` | Post a review and recalculate title rating | `201 Created` |
| `GET` | `/api/reviews/title/{titleId}` | Fetch all reviews for a movie | `200 OK` |
| `DELETE` | `/api/reviews/{id}` | Delete a review and update title stats | `204 No Content` |

---

## 🧪 Testing

The codebase includes unit tests written with **JUnit 5**, **Mockito**, and **AssertJ**, structured using `@Nested` classes matching CRUD operations.

To execute all tests:
```bash
./mvnw test
```

Test coverage focuses on:
* **Service Layer Happy Paths**: Verification of business logic, DTO mapping, and data persistence.
* **Edge Cases & Business Validation**: Duplicate prevention, non-existing parent entity handling (`TitleNotFoundException`, `PersonNotFoundException`).
* **JPA Dirty Checking**: Ensuring updates are applied seamlessly without redundant repository calls.

---

## ⚙️ Getting Started

### Prerequisites
* **JDK 21** or higher
* **Docker & Docker Compose**
* **Maven 3.8+** (or use the included `./mvnw` wrapper)

### 1. Clone the repository
```bash
git clone https://github.com/your-username/imdb-clone.git
cd imdb-clone
```

### 2. Start PostgreSQL via Docker Compose
Run the containerized PostgreSQL 15 instance:
```bash
docker compose up -d
```
*The database will be exposed locally at `localhost:5430` with persistent volume storage.*

### 3. Run the Backend Application
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

Interactive Swagger API docs will be available at:
👉 `http://localhost:8080/swagger-ui.html`

---

## 🗺️ Architectural Roadmap

- [x] **Phase 1: Core Domain, Services & REST API (Spring Boot + Postgres)**
- [x] **Phase 2: Comprehensive Unit Tests & Global Exception Handling (`@ControllerAdvice`)**
- [ ] **Phase 3: SPA Client (Angular 17+ with Signals, Tailwind CSS & Standalone Components)**
- [ ] **Phase 4: Security Layer (Spring Security + JWT Authentication)**
- [ ] **Phase 5: Polyglot Microservices Architecture**:
  - 🍃 **Java/Spring Cloud**: API Gateway & Core Catalog Services.
  - 🐍 **Python (FastAPI + Scikit-Learn)**: Recommendation Engine based on user watchlists & review sentiment analysis.
  - 🐹 **Go (Golang)**: Ultra-fast autocomplete search & real-time WebSocket notifications.
  - 📨 **Apache Kafka**: Event-driven asynchronous communication.
  - 🐳 **Docker Compose**: Containerized multi-service deployment.

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).