# PesoTrack: A Simple Personal Expense Tracker

PesoTrack is a multi-platform personal expense tracking system built for students who want a simple, consistent way to record and review their daily expenses. It consists of a Spring Boot REST API backend, a ReactJS web application, and an Android (Kotlin) mobile application, all sharing the same backend and database — so an expense logged on one platform is immediately reflected on the other.

**Course:** IT342-G01 — Systems Integration and Architecture 1
**Author:** Arnnon Zevv C. Pangan
**Institution:** Cebu Institute of Technology University

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Web App Setup](#web-app-setup)
  - [Mobile App Setup](#mobile-app-setup)
- [Environment Variables](#environment-variables)
- [API Overview](#api-overview)
- [Database Schema](#database-schema)
- [Deployment](#deployment)
- [Limitations / Out of Scope](#limitations--out-of-scope)
- [License](#license)

---

## Overview

Students often track expenses informally — in messaging apps, on scratch paper, or from memory — making it hard to see spending patterns over time. PesoTrack solves this with a lightweight, centralized system accessible from both a computer and a phone, backed by a single shared API and database.

## Features

- User registration and login (JWT-based authentication, shared between web and mobile)
- Add, edit, and delete expense records (amount, category, date, optional note)
- View a list of all expenses belonging to the logged-in user
- Consistent data across web and mobile through a shared backend API and database
- Passwords stored securely using one-way hashing (BCrypt)
- Authenticated API access — every expense operation requires a valid JWT token

**Not included in this version:** budgets/alerts, charts/reports, multi-currency support, bank account linking, offline mode.

## Tech Stack

| Layer      | Technology                                      |
|------------|--------------------------------------------------|
| Backend    | Java, Spring Boot, Spring Data JPA, Spring Security |
| Database   | PostgreSQL (hosted on Supabase) |
| Web        | ReactJS, Axios, Node.js |
| Mobile     | Android Studio, Kotlin, Retrofit |
| Deployment | Render (backend), Vercel (web app) |
| Versioning | Git and GitHub |

## Project Structure

```
PesoTrack/
├── backend/
│   └── PesoTracker/hellosummer/        # Spring Boot REST API (Maven project)
│       └── src/main/java/edu/cit/pangan/hellosummer/
│           ├── feature/
│           │   ├── auth/login/          # LoginController, LoginService, LoginRequest/Response
│           │   ├── auth/register/       # RegisterController, RegisterService, RegisterRequest/Response
│           │   ├── expense/add/         # AddExpenseController, AddExpenseService, ...
│           │   ├── expense/edit/        # EditExpenseController, EditExpenseService, ...
│           │   ├── expense/delete/      # DeleteExpenseController, DeleteExpenseService
│           │   ├── expense/view/        # ViewExpensesController, ViewExpensesService, ExpenseItem
│           │   └── users/list/          # ListUsersController, ListUsersService, UserSummary
│           └── shared/
│               ├── entity/              # User, Expense (JPA entities)
│               ├── repository/          # UserRepository, ExpenseRepository
│               └── security/            # JwtUtil, JwtAuthFilter, SecurityConfig, AuthenticatedUser
├── web/                                 # ReactJS web application (Vite)
│   └── src/
│       ├── pages/                       # Login, Register, Dashboard
│       ├── components/                  # ExpenseForm, ExpenseCard
│       └── api/                         # axiosInstance (JWT-attaching Axios client)
└── mobile/                              # Android (Kotlin) application
    └── app/src/main/java/edu/cit/pangan/pesotracker/
        ├── ui/                          # LoginActivity, RegisterActivity, DashboardActivity, AddEditExpenseActivity, ExpenseAdapter
        ├── data/network/                # ApiService (Retrofit), RetrofitClient
        ├── data/models/                 # Models.kt (request/response data classes)
        └── utils/                       # SessionManager
```

## Getting Started

### Prerequisites

- Java 17+ and Maven
- Node.js and npm
- Android Studio (for the mobile app)
- A PostgreSQL instance (e.g. a free [Supabase](https://supabase.com) project, or local Postgres)

### Backend Setup

```bash
cd backend/PesoTracker/hellosummer
export DB_URL=jdbc:postgresql://<your-host>:5432/postgres
export DB_USERNAME=postgres
export DB_PASSWORD=<your-db-password>
export JWT_SECRET=<a-long-random-256-bit-string>
./mvnw spring-boot:run
```
The backend runs on `http://localhost:8080` by default. Tables are created/updated automatically on startup (`spring.jpa.hibernate.ddl-auto=update`).

### Web App Setup

```bash
cd web
npm install
npm run dev
```
Create a `.env` file in `web/` with `VITE_API_URL=http://localhost:8080` pointing at the backend. The web app runs on `http://localhost:5173` by default (Vite's default dev port).

### Mobile App Setup

1. Open the `mobile/` folder in Android Studio.
2. Set `BASE_URL` in `RetrofitClient.kt` (`app/src/main/java/edu/cit/pangan/pesotracker/data/network/RetrofitClient.kt`) to your backend URL. It defaults to `http://10.0.2.2:8080/`, which lets the Android emulator reach a backend running on your PC's `localhost`. On a physical device on the same Wi-Fi, use your PC's local IP instead (e.g. `http://192.168.1.x:8080/`).
3. Build and run on an emulator or device running Android 8.0 (Oreo) or later.

## Environment Variables

| Variable          | Description                          | Example        |
|--------------------|---------------------------------------|-----------------|
| `DB_URL`           | Database connection URL              | `jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres` |
| `DB_USERNAME`      | Database username                    | `postgres` |
| `DB_PASSWORD`      | Database password                    | your Supabase/Postgres password |
| `JWT_SECRET`       | Secret key used to sign JWT tokens   | any long random string (256 bits recommended) |
| `jwt.expiration-ms`| Token expiration time (ms), set in `application.properties` | `86400000` (24 hours) — defaults to this value if unset |

> Note: the codebase reads these as `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` (see `application.properties`). Token expiration is configured via the `jwt.expiration-ms` property rather than a separate `JWT_EXPIRATION` env var, and already defaults to 24 hours if not overridden.

## API Overview

| Method | Endpoint             | Description                    | Auth Required |
|--------|-----------------------|---------------------------------|----------------|
| POST   | `/api/auth/register`  | Register a new user            | No             |
| POST   | `/api/auth/login`     | Log in and receive a JWT token | No             |
| GET    | `/api/expenses`       | List the logged-in user's expenses (newest first) | Yes |
| POST   | `/api/expenses`       | Add a new expense              | Yes            |
| PUT    | `/api/expenses/{id}`  | Edit an existing expense (must belong to the logged-in user) | Yes |
| DELETE | `/api/expenses/{id}`  | Delete an expense (must belong to the logged-in user) | Yes |
| GET    | `/api/users`          | List all registered users (admin/reference use) | Yes |

All authenticated endpoints expect an `Authorization: Bearer <token>` header, where `<token>` is the JWT returned by `/api/auth/login` or `/api/auth/register`.

## Database Schema

**User**
- `id` (PK)
- `fullname`
- `email`
- `username`
- `password` (hashed)
- `role`

**Expense**
- `id` (PK)
- `amount`
- `category`
- `date`
- `note` (optional)
- `user_id` (FK → User.id)

One `User` has zero or many `Expense` records; each `Expense` belongs to exactly one `User`.

## Deployment

Deployment targets are configured but the app has not been deployed to a live URL yet — it currently runs locally for development.

- **Backend:** Intended for Render. `web/.env.production` already points `VITE_API_URL` at a placeholder Render URL (`https://your-backend.onrender.com`) to be replaced once the backend is deployed.
- **Web App:** Intended for Vercel, built with `npm run build` (outputs to `web/dist`).
- **Database:** Supabase-hosted PostgreSQL (see `application.properties`), reachable from both local and deployed backends via the `DB_URL`/`DB_USERNAME`/`DB_PASSWORD` environment variables.

## Limitations / Out of Scope

This initial version does not include:
- Budget setting, spending limits, or alerts/notifications
- Charts, graphs, or generated financial reports
- Multi-currency support
- Bank account linking or automatic transaction import
- Offline mode for the mobile application

## License

Academic project — developed for course requirements at Cebu Institute of Technology University and not licensed for external/commercial use.