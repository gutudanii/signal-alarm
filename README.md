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

| Endpoint                  | Method | Description                       |
|--------------------------|--------|-----------------------------------|
| /auth/register           | POST   | Register a new user               |
| /auth/login              | POST   | User login                        |
| /auth/oauth2/create      | GET    | Google OAuth2 user creation       |
| /devices                 | POST   | Register a device                 |
| /users/{id}              | GET    | Get user by ID                    |
| /producer                | POST   | Create a producer                 |

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
- Environment variables (JWT secret, DB URL, etc.)

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

> **What is this system?** Modular backend for signals & notifications.
> **What can it do?** Auth, user/device/producer management, notifications.
> **How is it structured?** Modular services, secure, scalable.
> **How do I run it?** See Quick Start and Environment Setup above.
