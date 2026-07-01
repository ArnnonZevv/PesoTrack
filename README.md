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
| Database   | MySQL |
| Web        | ReactJS, Axios, Node.js |
| Mobile     | Android Studio, Kotlin, Retrofit |
| Deployment | Render (backend), Vercel (web app) |
| Versioning | Git and GitHub |

## Project Structure

```
Replace
```
> Replace this block with your actual repository layout, e.g.:
> ```
> pesotrack/
> ├── backend/     # Spring Boot REST API
> ├── web/         # ReactJS web application
> └── mobile/      # Android (Kotlin) application
> ```

## Getting Started

### Prerequisites

- Java 17+ and Maven
- Node.js and npm
- Android Studio (for the mobile app)
- MySQL instance (local or hosted)

### Backend Setup

```bash
cd backend
Replace
```
> Replace with actual setup/run commands, e.g. `./mvnw spring-boot:run`. The backend runs on `http://localhost:Replace` by default.

### Web App Setup

```bash
cd web
npm install
npm start
```
> Replace port/URL details as needed. The web app runs on `http://localhost:Replace` by default.

### Mobile App Setup

1. Open the `mobile/` folder in Android Studio.
2. Replace the base API URL in the app's network configuration with your backend URL.
3. Build and run on an emulator or device running Android 8.0 (Oreo) or later.

## Environment Variables

| Variable          | Description                          | Example        |
|--------------------|---------------------------------------|-----------------|
| `DB_URL`           | Database connection URL              | Replace |
| `DB_USERNAME`      | Database username                    | Replace |
| `DB_PASSWORD`      | Database password                    | Replace |
| `JWT_SECRET`       | Secret key used to sign JWT tokens   | Replace |
| `JWT_EXPIRATION`   | Token expiration time (ms)           | Replace |

## API Overview

| Method | Endpoint             | Description                    | Auth Required |
|--------|-----------------------|---------------------------------|----------------|
| POST   | `/api/auth/register`  | Register a new user            | No             |
| POST   | `/api/auth/login`     | Log in and receive a JWT token | No             |
| GET    | `/api/expenses`       | List the logged-in user's expenses | Yes        |
| POST   | `/api/expenses`       | Add a new expense              | Yes            |
| PUT    | `/api/expenses/{id}`  | Edit an existing expense       | Yes            |
| DELETE | `/api/expenses/{id}`  | Delete an expense              | Yes            |

> Replace with actual endpoint paths and request/response payloads once finalized.

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

- **Backend:** Deployed on Render — Replace
- **Web App:** Deployed on Vercel — Replace

## Limitations / Out of Scope

This initial version does not include:
- Budget setting, spending limits, or alerts/notifications
- Charts, graphs, or generated financial reports
- Multi-currency support
- Bank account linking or automatic transaction import
- Offline mode for the mobile application

## License

Replace (e.g., MIT, or "Academic project — not licensed for external use")
