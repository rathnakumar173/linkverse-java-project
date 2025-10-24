# LinkVerse: A Secure, Team-Based Link Management System

LinkVerse is a robust, production-ready backend service for a team-based link management and bookmarking application. Built with Java, Spring Boot, and Spring Security, it provides a secure, multi-tenant environment for users to create teams, share bookmarks, and categorize links with tags.

This project is designed to showcase enterprise-level backend development skills, including secure JWT authentication, a complex relational data model (Many-to-Many), a clean layered architecture, and a professional REST API design.

## Features

* **Secure JWT Authentication:** Full user registration and login system using Spring Security and JSON Web Tokens.
* **Team Management:** Users can create and join multiple teams.
* **Link Management:** Full CRUD (Create, Read, Update, Delete) functionality for links (bookmarks).
* **Team-Based Authorization:** Links are scoped to teams. Only members of a team can add, view, or manage links within that team.
* **Tagging System:** A flexible Many-to-Many tagging system to categorize links.
* **Professional API Design:**
    * **Layered Architecture:** (Controller -> Service -> Repository) for separation of concerns.
    * **DTOs (Data Transfer Objects):** All API communication uses DTOs, not raw entities, for security and API stability.
    * **Request Validation:** Input validation for all API endpoints.
    * **Global Exception Handling:** Clean, consistent JSON error responses for all exceptions.

## Tech Stack

* **Backend:** Java 17, Spring Boot 3.x
* **Security:** Spring Security, JSON Web Tokens (JWT)
* **Database:** Spring Data JPA (Hibernate), MySQL 8
* **Build Tool:** Maven
* **Utilities:** Lombok, `jjwt` (Java JWT)
* **API Testing:** Postman

## Project Architecture

The application follows a standard 3-layer architecture:

1.  **Controller Layer (`/controller`):** Handles all incoming HTTP requests, validates input (via DTOs), and delegates business logic to the Service layer.
2.  **Service Layer (`/service`):** Contains all business logic, handles transactions, and coordinates between repositories. It's the core of the application.
3.  **Repository Layer (`/repository`):** Manages data access using Spring Data JPA. It defines interfaces for database operations on the entities.

## API Endpoints

A brief overview of the available API endpoints.

### Authentication (`/api/v1/auth`)

* `POST /api/v1/auth/register`: Register a new user.
* `POST /api/v1/auth/login`: Log in and receive a JWT token.

### Teams (`/api/v1/teams`)

* `POST /api/v1/teams`: Create a new team (user automatically becomes a member).
* `GET /api/v1/teams`: Get all teams the authenticated user is a member of.
* `GET /api/v1/teams/{teamId}`: Get details for a specific team.
* `POST /api/v1/teams/{teamId}/add-member/{userId}`: Add another user to a team.

### Links (`/api/v1/links`)

* `POST /api/v1/links`: Create a new link for a specific team.
* `GET /api/v1/links/team/{teamId}`: Get all links for a specific team.
* `GET /api/v1/links/{linkId}`: Get a specific link by its ID.
* `PUT /api/v1/links/{linkId}`: Update a link.
* `DELETE /api/v1/links/{linkId}`: Delete a link.

## Setup and Run

1.  **Prerequisites:**
    * Java JDK 17
    * Maven
    * MySQL Server 8

2.  **Database Setup:**
    * Open your MySQL client and run:
        ```sql
        CREATE DATABASE linkverse_db;
        ```

3.  **Configuration:**
    * Open `src/main/resources/application.properties`.
    * Update the `spring.datasource.username` and `spring.datasource.password` properties with your MySQL credentials.
    * Update the `app.jwt.secret` with a custom, long, and random string.

4.  **Run the Application:**
    * Open a terminal in the project's root directory.
    * Run the application using Maven:
        ```bash
        mvn spring-boot:run
        ```
    * The application will start, and Hibernate will automatically create all tables.