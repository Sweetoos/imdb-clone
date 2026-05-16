Oto kompletny projekt bazy danych dla klona IMDb, uwzględniający klucze (PK, FK), ograniczenia (NOT NULL, UNIQUE) oraz typy danych.

W Hibernate będziesz używać tych nazw kolumn w adnotacjach `@Column` i `@JoinColumn`.

---

### 1. Tabela `titles` (Filmy i Seriale)
Główna tabela przechowująca wszystkie produkcje.

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `id` | VARCHAR(20) | **PK**, NN | Np. "tt0111161" (IMDb style) lub SERIAL |
| `title_type` | VARCHAR(20) | NN | movie, tvSeries, tvEpisode, short |
| `primary_title` | VARCHAR(500) | NN | Tytuł główny |
| `original_title` | VARCHAR(500) | | Tytuł oryginalny |
| `is_adult` | BOOLEAN | NN, DEFAULT false | Czy dla dorosłych |
| `start_year` | INTEGER | | Rok wydania |
| `end_year` | INTEGER | | Rok zakończenia (dla seriali) |
| `runtime_minutes`| INTEGER | | Czas trwania |

---

### 2. Tabela `names` (Ludzie: Aktorzy, Reżyserzy)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `id` | VARCHAR(20) | **PK**, NN | Np. "nm0000151" lub SERIAL |
| `primary_name` | VARCHAR(255) | NN | Imię i nazwisko |
| `birth_year` | INTEGER | | Rok urodzenia |
| `death_year` | INTEGER | | Rok śmierci |

---

### 3. Tabela `genres` (Gatunki)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `id` | SERIAL | **PK**, NN | Klucz główny |
| `name` | VARCHAR(50) | NN, **UNIQUE** | Nazwa (Drama, Comedy, Sci-Fi) |

---

### 4. Tabela `title_genres` (Relacja Many-to-Many)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `title_id` | VARCHAR(20) | **PK**, **FK**, NN | Odniesienie do `titles(id)` |
| `genre_id` | INTEGER | **PK**, **FK**, NN | Odniesienie do `genres(id)` |

---

### 5. Tabela `title_principals` (Obsada i Ekipa - Many-to-Many z danymi)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `title_id` | VARCHAR(20) | **PK**, **FK**, NN | Odniesienie do `titles(id)` |
| `name_id` | VARCHAR(20) | **PK**, **FK**, NN | Odniesienie do `names(id)` |
| `ordering` | INTEGER | **PK**, NN | Kolejność wyświetlania na liście |
| `category` | VARCHAR(100) | NN | actor, director, writer, producer |
| `job` | VARCHAR(255) | | Konkretne stanowisko (opcjonalnie) |
| `characters` | TEXT | | Nazwa postaci (np. jako JSON string) |

---

### 6. Tabela `title_episodes` (Powiązania Serial -> Odcinek)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `episode_id` | VARCHAR(20) | **PK**, **FK**, NN, **U** | ID odcinka (odnosi się do `titles.id`) |
| `parent_title_id`| VARCHAR(20) | **FK**, NN | ID serialu (odnosi się do `titles.id`) |
| `season_number` | INTEGER | | Numer sezonu |
| `episode_number` | INTEGER | | Numer odcinka |

---

### 7. Tabela `title_ratings` (Agregacja Ocen)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `title_id` | VARCHAR(20) | **PK**, **FK**, NN, **U** | Odniesienie do `titles(id)` |
| `average_rating` | DECIMAL(3,1) | NN, DEFAULT 0.0 | Średnia ocen (np. 8.4) |
| `num_votes` | INTEGER | NN, DEFAULT 0 | Liczba oddanych głosów |

---

### 8. Tabela `users` (Użytkownicy)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `id` | SERIAL | **PK**, NN | Klucz główny |
| `username` | VARCHAR(50) | NN, **UNIQUE** | Nazwa użytkownika |
| `email` | VARCHAR(255) | NN, **UNIQUE** | Adres e-mail |
| `password_hash` | VARCHAR(255) | NN | Zahashowane hasło |
| `created_at` | TIMESTAMP | NN, DEFAULT NOW() | Data rejestracji |

---

### 9. Tabela `reviews` (Recenzje Użytkowników)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `id` | SERIAL | **PK**, NN | Klucz główny |
| `user_id` | INTEGER | **FK**, NN | Kto napisał |
| `title_id` | VARCHAR(20) | **FK**, NN | Co ocenił |
| `rating` | INTEGER | NN | Ocena 1-10 (**CHECK** rating BETWEEN 1 AND 10) |
| `review_text` | TEXT | | Treść recenzji |
| `created_at` | TIMESTAMP | NN, DEFAULT NOW() | Data dodania |
| **Unique Constraint** | | `(user_id, title_id)` | Jeden użytkownik = jedna recenzja filmu |

---

### 10. Tabela `watchlists` (Relacja Many-to-Many)

| Kolumna | Typ | Ograniczenia | Opis |
| :--- | :--- | :--- | :--- |
| `user_id` | INTEGER | **PK**, **FK**, NN | Odniesienie do `users(id)` |
| `title_id` | VARCHAR(20) | **PK**, **FK**, NN | Odniesienie do `titles(id)` |
| `added_at` | TIMESTAMP | NN, DEFAULT NOW() | Kiedy dodano do listy |

---

### Legenda oznaczeń:
*   **PK**: Primary Key (Klucz główny).
*   **FK**: Foreign Key (Klucz obcy).
*   **NN**: Not Null (Pole nie może być puste).
*   **U**: Unique (Wartość musi być unikalna w całej tabeli).
*   **SERIAL**: Automatycznie inkrementowany klucz całkowity (w PostgreSQL).
*   **VARCHAR(20)**: Jeśli planujesz importować dane z IMDb, użyj VARCHAR, bo ich ID to stringi typu `tt0000001`. Jeśli tworzysz własne dane, możesz użyć `BIGSERIAL`.