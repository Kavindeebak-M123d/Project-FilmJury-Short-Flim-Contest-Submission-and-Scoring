# FilmJury – Short Film Contest Management System

FilmJury is a complete, working Spring Boot 3 web application designed for managing college short film contests. It allows team film submissions, jury management, independent judge assignment, multi-criteria scoring, automatic average score calculation, and a real-time leaderboard.

---

## Features

- **Home Page**: Professional cinema-themed landing page with feature highlights and recent contest submissions.
- **Film Submission**: Form for teams to submit films with title, genre, team name, crew members, video link, and synopsis.
- **Entries Management**: Browse, search by title/team, filter by genre, sort by date/score/title, and view film details.
- **Jury Management**: Add, edit, delete, and view jury members with unique email validation.
- **Judge Assignment**: Assign multiple jury members to specific films without duplicates.
- **Scoring System**: Independent scoring for assigned judges across 5 customizable criteria (Story, Creativity, Cinematography, Acting, Editing) with slider controls and feedback comments.
- **Business Rules Enforcement**:
  1. Average score calculated dynamically across all judge scorecards.
  2. Strict duplicate score prevention per judge/entry.
  3. Strict unassigned judge scoring prevention.
  4. Automatic leaderboard updates upon score submission.
- **Live Leaderboard**: Rank films dynamically with medal badges (🥇 1st, 🥈 2nd, 🥉 3rd) showing average scores and judge counts.
- **Criteria Management**: Admin page to add, edit, or delete criteria and max score bounds.
- **Admin Dashboard**: System metrics overview, quick action buttons, top films, recent submissions, and live scoring activity.
- **REST API Endpoints**: Full set of RESTful endpoints under `/api/...` for entries, judges, criteria, scorecards, and leaderboard.

---

## Tech Stack

- **Java**: Java 17+ (or Java 21)
- **Framework**: Spring Boot 3.2.3
- **Persistence**: Spring Data JPA & Hibernate
- **Database**: MySQL (with automatic database creation support)
- **Validation**: Spring Validation (`@NotBlank`, `@Email`, `@Min`, `@Max`)
- **Frontend**: Thymeleaf + HTML5 + CSS3 (Cinema Dark Theme) + JavaScript + Bootstrap 5 + FontAwesome 6

---

## Step-by-Step Setup Instructions

### 1. Prerequisites & Java Installation
- Ensure **Java 17** or **Java 21** JDK is installed on your system.
- Verify installation in terminal:
  ```bash
  java -version
  ```

### 2. MySQL Database Setup
1. Start your MySQL Server (via MySQL Workbench, XAMPP, Command Line, or Docker).
2. Create the database `filmjury_db`:
   ```sql
   CREATE DATABASE IF NOT EXISTS filmjury_db;
   ```
   *(Note: The `application.properties` is configured with `createDatabaseIfNotExist=true`, so Spring Boot can automatically create the database if permissions allow).*

### 3. Open Project in IntelliJ IDEA
1. Launch **IntelliJ IDEA**.
2. Select **Open** and browse to the project folder (`FilmJury`).
3. IntelliJ will automatically detect Maven and import dependencies defined in `pom.xml`.
4. Wait for Maven indexing to complete.

### 4. Configure `application.properties`
Open `src/main/resources/application.properties` and adjust your MySQL credentials:
```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/filmjury_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
*Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password.*

### 5. Running the Application
1. In IntelliJ IDEA, navigate to `src/main/java/com/example/filmjury/FilmJuryApplication.java`.
2. Right-click `FilmJuryApplication.java` and select **Run 'FilmJuryApplication'** (or press `Shift + F10`).
3. Alternatively, run via terminal:
   ```bash
   mvn spring-boot:run
   ```

### 6. Accessing the Web Application
Open your browser and navigate to:
```
http://localhost:8080
```

---

## User & Workflow Testing Guide

1. **Automatic Sample Data**:
   - On first startup, sample criteria (*Story, Creativity, Cinematography, Acting, Editing*), sample judges (*Christopher Nolan, Greta Gerwig, Guillermo del Toro*), sample films, and initial scorecards are automatically seeded into the database!

2. **Test Film Submission**:
   - Go to **Submit Film** (`/entries/new`).
   - Fill in Team Name, Film Title, Genre, Video Link, and Description.
   - Click **Submit Film**. You will see a green success alert and be redirected to the **Entries** page (`/entries`).

3. **Add / Edit Judges**:
   - Go to **Judges** (`/judges`).
   - Add a new judge (e.g., Name: *Quentin Tarantino*, Email: *quentin@filmjury.org*).
   - Duplicate email validation prevents adding duplicate accounts.

4. **Assign Judges to a Film**:
   - Navigate to **Entries** and click **Assign** on any film (or view film details and click **Assign Judges**).
   - Check the boxes for the judges assigned to review the film.
   - Click **Assign Judges**.

5. **Submit Judge Scores**:
   - Click **Score** on an entry (`/entries/{id}/score`).
   - Select an assigned judge from the dropdown.
   - Adjust criterion score sliders (0 - 10). Notice the **Live Average Calculation** preview.
   - Add feedback comments and click **Submit Score**.
   - If the judge is not assigned, or if the judge tries to submit a duplicate scorecard, the service layer rejects it with a clear error message!

6. **View Live Leaderboard**:
   - Navigate to **Leaderboard** (`/leaderboard`).
   - Observe films dynamically ranked by average score in descending order with gold 🥇, silver 🥈, and bronze 🥉 rank badges!

---

## REST API Documentation

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/entries` | Get all film entries |
| `GET` | `/api/entries/{id}` | Get entry details by ID |
| `POST` | `/api/entries` | Submit a new film entry |
| `PUT` | `/api/entries/{id}` | Update film entry details |
| `DELETE` | `/api/entries/{id}` | Delete a film entry |
| `POST` | `/api/entries/{entryId}/judges/{judgeId}` | Assign a judge to a film |
| `GET` | `/api/judges` | Get all judges |
| `POST` | `/api/judges` | Register a new judge |
| `PUT` | `/api/judges/{id}` | Update judge info |
| `DELETE` | `/api/judges/{id}` | Delete a judge |
| `GET` | `/api/criteria` | Get all scoring criteria |
| `POST` | `/api/criteria` | Add new criterion |
| `POST` | `/api/scorecards` | Submit judge scorecard JSON |
| `GET` | `/api/leaderboard` | Get ranked leaderboard JSON |
