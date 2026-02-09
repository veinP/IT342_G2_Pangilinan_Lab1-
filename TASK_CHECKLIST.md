# Task Checklist - HealthGate Authentication System

## DONE

| Task | Description | Commit Hash |
|------|-------------|-------------|
| Backend Setup | Spring Boot project with MySQL, JPA, Security, JWT dependencies | `<add-commit-hash>` |
| User Entity | Created User model with JPA annotations matching ERD | `<add-commit-hash>` |
| User Repository | Created JPA repository with findByEmail and existsByEmail | `<add-commit-hash>` |
| DTOs | Created RegisterRequest, LoginRequest, LoginResponse, ApiResponse | `<add-commit-hash>` |
| JWT Provider | JWT token generation, validation, and extraction utility | `<add-commit-hash>` |
| JWT Auth Filter | Request filter to validate JWT tokens on protected routes | `<add-commit-hash>` |
| Security Config | Spring Security config with BCrypt, CORS, stateless sessions | `<add-commit-hash>` |
| Auth Service | Business logic for register, login, logout, getCurrentUser | `<add-commit-hash>` |
| Auth Controller | POST /api/auth/register, POST /api/auth/login, POST /api/auth/logout | `<add-commit-hash>` |
| User Controller | GET /api/user/me (protected endpoint) | `<add-commit-hash>` |
| Password Encryption | BCrypt password hashing for secure storage | `<add-commit-hash>` |
| React Web App Setup | Vite + React project with axios, react-router-dom | `<add-commit-hash>` |
| Register Page | Registration form with validation and API integration | `<add-commit-hash>` |
| Login Page | Login form with JWT token storage | `<add-commit-hash>` |
| Dashboard/Profile Page | Protected page displaying user profile information | `<add-commit-hash>` |
| Logout Functionality | Token invalidation and redirect to login | `<add-commit-hash>` |
| Auth Context | React context for global auth state management | `<add-commit-hash>` |
| Protected Route | Route guard component for authenticated-only pages | `<add-commit-hash>` |
| API Service | Axios instance with interceptors for JWT auth | `<add-commit-hash>` |

## IN-PROGRESS

| Task | Description |
|------|-------------|
| Documentation | FRS PDF with ERD, UML diagrams, and Web UI screenshots |

## TODO

| Task | Description |
|------|-------------|
| Mobile App (React Native) | Register, Login, Dashboard, Logout screens |
| Mobile API Integration | Connect mobile app to Spring Boot backend |
| FRS Update - Mobile Screenshots | Add mobile UI screenshots to FRS document |
| Deployment | Deploy backend and web app to production |
