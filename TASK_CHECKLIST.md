# Task Checklist - HealthGate Authentication System

## DONE

| Task | Description | Commit Hash |
|------|-------------|-------------|
| Backend Setup | Spring Boot project with MySQL, JPA, Security, JWT dependencies | `94967c7` |
| User Entity | Created User model with JPA annotations matching ERD | `1fe833f` |
| User Repository | Created JPA repository with findByEmail and existsByEmail | `9c04ad4` |
| DTOs | Created RegisterRequest, LoginRequest, LoginResponse, ApiResponse | `a0e46cf` |
| JWT Provider | JWT token generation, validation, and extraction utility | `2e80179` |
| JWT Auth Filter | Request filter to validate JWT tokens on protected routes | `6d89160` |
| Security Config | Spring Security config with BCrypt, CORS, stateless sessions | `6a72f65` |
| Auth Service | Business logic for register, login, logout, getCurrentUser | `132c835` |
| Auth Controller | POST /api/auth/register, POST /api/auth/login, POST /api/auth/logout | `482f503` |
| User Controller | GET /api/user/me (protected endpoint) | `a521c60` |
| Password Encryption | BCrypt password hashing for secure storage | `b1da0eb` |
| React Web App Setup | Vite + React project with axios, react-router-dom | `d5f817a` |
| Register Page | Registration form with validation and API integration | `c43a2be` |
| Login Page | Login form with JWT token storage | `9a11f7e` |
| Dashboard/Profile Page | Protected page displaying user profile information | `9610381` |
| Logout Functionality | Token invalidation and redirect to login | `b122776` |
| Auth Context | React context for global auth state management | `b122776` |
| Protected Route | Route guard component for authenticated-only pages | `82f5c10` |
| API Service | Axios instance with interceptors for JWT auth | `b9f06e1` |
| UI Color Scheme | Unified color palette (#7C94B8, #2A4D87, #D9D9D7, #B1BBC7) across all pages | `112f6ec` |
| Logout Confirmation | Modal card with confirm/cancel before logging out | `112f6ec` |
| Documentation | FRS PDF with ERD, UML diagrams, and Web UI screenshots | — |

## IN-PROGRESS

_No tasks in progress._

## TODO

| Task | Description |
|------|-------------|
| Mobile App (React Native) | Register, Login, Dashboard, Logout screens |
| Mobile API Integration | Connect mobile app to Spring Boot backend |
| FRS Update - Mobile Screenshots | Add mobile UI screenshots to FRS document |
| Deployment | Deploy backend and web app to production |
