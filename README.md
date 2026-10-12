# 🎬 IMDb Clone — Movie & TV Series Platform

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular&logoColor=white)

An in-progress clone of IMDb (Internet Movie Database), with a Java/Spring Boot REST API and an Angular frontend. The backend is organized into domain modules with controllers, services, repositories, and DTOs.

---

## 🚀 Key Features

* **🎥 Title Management:** Support for Movies, TV Shows, and Episodes with hierarchical relationships.
* **🎭 Cast & Crew Filmography:** Many-to-many relationship linking actors, directors, writers, and producers with specific character roles.
* **🏷️ Rich Metadata:** Tagging and categorization through dynamic **Genres**, **Keywords**, and **Localized Titles (AKAs)**.
* **⭐ User Reviews & Ratings:** Unique rating submissions per user, automated average rating computation, and user watchlists.
* **🛠️ Backend Structure:** DTOs, custom business exceptions, Spring Data JPA, and Swagger/OpenAPI documentation.

---

## 📚 Tutorial

For a beginner-friendly explanation in Polish of the implemented authentication and Angular flow, see [Security i Angular — tutorial](docs/SECURITY_I_ANGULAR_TUTORIAL.md).

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 25
* **Framework:** Spring Boot 4.0.5 (Spring Web MVC, Spring Data JPA)
* **Database:** PostgreSQL 15 (Docker containerized)
* **Security & Auth:** Spring Security, BCrypt password hashing, and stateless JWT authentication with JJWT 0.13.0.
* **Utilities:** Project Lombok (Builder pattern, immutability)
* **API Documentation:** SpringDoc OpenAPI / Swagger UI
* **Testing:** JUnit 5, Mockito, AssertJ

### Frontend (In Progress)
* **Framework:** Angular 21.2 (Standalone Components, Signals, RxJS)
* **Styling:** Tailwind CSS 4 and component CSS

### Current Frontend & Security Status

* The frontend lives in `web/` and includes a header, home page, title card, and login panel.
* The home page currently uses a hardcoded sample title; catalog integration is in progress.
* The login panel supports registration, login, logout, and session restoration through `/auth/me`.
* The Angular interceptor adds Bearer tokens only to this backend's requests, excludes login/register requests, and clears the session on a 401 response.
* Tokens are stored in browser local storage. Logout removes the local token; issued tokens remain valid until expiration (one hour by default).

### Authentication API & Access Rules

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/auth/register` | Register with `username`, `email`, and `password`; returns `201` with `{token, user}` |
| `POST` | `/auth/login` | Authenticate with `username` and `password`; returns `{token, user}` |
| `GET` | `/auth/me` | Return the current user; requires `Authorization: Bearer <token>` |

* Registration requires a 3–50 character username, valid email, and an 8–72 character password. BCrypt additionally limits passwords to 72 UTF-8 bytes.
* Catalog and review GET endpoints and Swagger documentation are public. Catalog changes require authentication; the current model has no admin role, so every authenticated user can modify catalog data.
* Users can read, update, and delete only their own account. Listing all users and the old `POST /api/users` route are denied; registration uses `/auth/register`.
* Review creation requires the authenticated user's ID. Review edits/deletion require ownership. Watchlists can be read or changed only by their owner.
* Missing, invalid, expired, or deleted-user tokens return `401`; denied access returns `403`.
* Passwords are stored as BCrypt hashes. Existing plaintext or invalid placeholder hashes cannot be used for login; register a new account for testing.
* For local development, an empty `JWT_SECRET` generates an ephemeral signing key at startup, invalidating previous tokens after restart. For a persistent deployment, set `JWT_SECRET` to a Base64-encoded random key of at least 32 bytes. `JWT_EXPIRATION` is in milliseconds and defaults to `3600000`.

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
* **JDK 25**
* **Docker & Docker Compose**
* **Maven** (or use the included `./mvnw` wrapper)
* **Node.js and npm** for the Angular frontend (the frontend declares npm 11.12.1)

### 1. Open the repository
Use a local checkout of this repository and open a terminal in its root directory.

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

### 4. Run the Frontend

In a separate terminal:
```bash
cd web
npm install
npm start
```
The Angular development server is available at `http://localhost:4200`.

To build the frontend:
```bash
npm run build
```

Open `/login-panel` to register or log in. The backend must be running at `http://localhost:8080`.

---

## 🗺️ Architectural Roadmap

- [x] **Phase 1: Core Domain, Services & REST API (Spring Boot + Postgres)**
- [x] **Phase 2: Service Unit Tests & Global Exception Handling (`@ControllerAdvice`)**
- [ ] **Phase 3 (in progress): SPA Client (Angular 21 with Signals, Tailwind CSS & Standalone Components)**
- [x] **Phase 4: Security Layer (Spring Security + JWT Authentication)**
- [ ] **Phase 5: Polyglot Microservices Architecture**:
  - 🍃 **Java/Spring Cloud**: API Gateway & Core Catalog Services.
  - 🐍 **Python (FastAPI + Scikit-Learn)**: Recommendation Engine based on user watchlists & review sentiment analysis.
  - 🐹 **Go (Golang)**: Ultra-fast autocomplete search & real-time WebSocket notifications.
  - 📨 **Apache Kafka**: Event-driven asynchronous communication.
  - 🐳 **Docker Compose**: Containerized multi-service deployment.

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).