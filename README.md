# Signal Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://shields.io/) [![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A robust, modular backend for authentication, user management, device registration, and signal provider management.

---

## Purpose

Signal Platform provides:
- Secure authentication & user management
- Device registration for notifications
- Producer (signal provider) management

---

## Modules

- 👤 **User Service** — User registration, profile, and role management
- 🔐 **Authentication Service** — JWT & OAuth2 (Google) authentication
- 📱 **Device Service** — Device registration for push notifications
- 📢 **Producer Service** — Signal provider creation & management

---

## Tech Stack

- **Backend:** Spring Boot
- **Security:** Spring Security, JWT, OAuth2 (Google)
- **Database:** PostgreSQL
- **Frontend:** Flutter (Android & iOS)
- **Notifications:** Firebase Cloud Messaging (FCM), Telegram Bot

---

## Core Features

- User registration & login
- Google login (OAuth2)
- Role-based access (USER / PRODUCER)
- Device registration for push notifications
- Producer creation & management
- Notification delivery (App + Telegram)

---

## Architecture Overview

- **Single Auth Service:** Handles authentication, authorization, subscription
- **User Management:** Stores user data & roles
- **Device Management:** Stores FCM tokens
- **Producer Management:** Manages signal providers

---

## API Overview

| Method | Endpoint              | Description                       |
|--------|----------------------|-----------------------------------|
| POST   | /auth/register       | Register new user                 |
| POST   | /auth/login          | Login with email/password         |
| GET    | /oauth2/authorization/google | Google OAuth2 login         |
| POST   | /devices             | Register device for notifications |
| GET    | /users/{id}          | Get user by ID                    |
| POST   | /producer            | Create new producer               |

---

## Security Approach

- JWT-based authentication
- OAuth2 (Google login)
- Role-based authorization
- Password hashing (BCrypt)

---

## Environment Setup

- Java 17+
- PostgreSQL
- Firebase configuration
- Environment variables (JWT secret, DB URL, Google client secret, etc.)

---

## Quick Start

1. Clone the repository:
   ```sh
   git clone https://github.com/your-org/signal-platform.git
   cd signal-platform
   ```
2. Configure environment variables in `application.properties`.
3. Start PostgreSQL and set up the database.
4. Build and run the backend:
   ```sh
   ./mvnw spring-boot:run
   ```
5. (Optional) Set up Firebase and Telegram Bot for notifications.

---

## Contributing

Contributions are welcome! Please open issues or submit pull requests for improvements.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

> **What is this system?**
> Modular backend for authentication, device, and producer management.
>
> **What can it do?**
> Register/login users (Google or email), manage devices, send notifications, manage producers.
>
> **How is it structured?**
> Microservice-style modules for user, device, producer, and authentication.
>
> **How do I run it?**
> Configure environment variables, set up PostgreSQL, and run with Java 17+.
