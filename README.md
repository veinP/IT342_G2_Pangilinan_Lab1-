# HealthGate - User Registration and Authentication System

A secure user authentication system built with **Spring Boot** (backend) and **React** (web frontend).

## Project Structure

```
IT342_G4_Pangilinan_Lab1/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/pangilinan/userauth/
│   │   ├── config/             # Security configuration
│   │   ├── controller/         # REST API controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── model/              # JPA Entity models
│   │   ├── repository/         # Data access layer
│   │   ├── security/           # JWT provider & filter
│   │   └── service/            # Business logic
│   └── pom.xml
├── web/                        # React Web Application
│   └── src/
│       ├── components/         # Reusable components
│       ├── context/            # Auth context provider
│       ├── pages/              # Page components
│       └── services/           # API service layer
├── mobile/                     # Mobile App (upcoming)
├── docs/                       # Documentation (FRS, diagrams)
├── README.md
└── TASK_CHECKLIST.md
```

## Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.5**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with MySQL
- **BCrypt** password encryption

### Web Frontend
- **React 19** with Vite
- **React Router** for navigation
- **Axios** for API communication

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |
| POST | `/api/auth/logout` | Logout and invalidate token | Yes |
| GET | `/api/user/me` | Get current user profile | Yes |

## Prerequisites

- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven

## Setup & Run

### 1. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE healthgate_db;
```

### 2. Backend Configuration
Update `backend/src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Run Backend
```bash
cd backend
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8080`

### 4. Run Web Application
```bash
cd web
npm install
npm run dev
```
Web app runs on `http://localhost:5173`

## Features

- ✅ User Registration with input validation
- ✅ User Login with JWT token authentication
- ✅ Protected Dashboard/Profile page
- ✅ Secure Logout with token invalidation
- ✅ BCrypt password encryption
- ✅ CORS configuration for frontend-backend communication
- ✅ Responsive UI design
