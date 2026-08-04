# Train Seat Booking System

## Overview

The **Train Seat Booking System** is a full-stack web application developed to modernize the reservation process for Sri Lanka's **Colombo Fort – Badulla** railway line. Unlike conventional railway reservation systems where a reserved seat remains unavailable for the remainder of the journey once booked, this system introduces **segment-based seat booking**, allowing the same physical seat to be reserved by multiple passengers travelling on different, non-overlapping segments of the route.

For example, if one passenger books Seat 15 from **Colombo Fort → Kandy**, another passenger can subsequently reserve the same Seat 15 from **Kandy → Badulla**, increasing seat utilization while ensuring that overlapping journeys are never assigned the same seat.

The system has been designed as a production-ready application with configurable routes, trains, coaches, seats, and schedules, making it adaptable for future railway expansions without requiring code changes.

---

# Key Features

### Passenger Features

- Search available train schedules by:
  - Origin station
  - Destination station
  - Departure time

- View available reserved seats for the selected journey.
- Graphical seat selection.
- Segment-based seat allocation.
- Multiple-seat booking in a single transaction.
- Automatic booking conflict prevention.
- Booking confirmation with a unique order reference.
- Retrieve booking details using the order reference.

### Administrator Features

- Manage stations.
- Manage routes and route stations.
- Manage trains.
- Manage coaches.
- Automatic seat generation when coaches are created.
- Manage train schedules.
- Manage schedule stop timings.
- View booking orders.
- View dashboard analytics including:
  - Total orders
  - Revenue
  - Confirmed bookings
  - Cancelled bookings
  - Active schedules
  - Overall occupancy rate

---

# Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- SpringDoc OpenAPI (Swagger)

## Frontend

- React
- Vite
- Axios
- React Router

## Infrastructure

- Docker
- Docker Compose
- PostgreSQL
- Nginx

---

# Project Structure

```
train-seat-booking/
│
├── backend/
│
├── frontend/
│
├── database/
│   ├── train_booking.dump
│   └── 01-restore-database.sh
│
├── docker-compose.yml
│
├── .env.example
│
└── README.md
```

---

# Running the Application

## Prerequisites

Install the following software:

- Docker Desktop
- Git

No additional software is required.

The following are **not required** on the host machine:

- Java
- Maven
- Node.js
- npm
- PostgreSQL

Everything required to run the application is provided through Docker.

---

# Clone the Repository

```bash
git clone <repository-url>

cd train-seat-booking
```

---

# Environment Configuration

Create a `.env` file by copying the example configuration.

Linux / macOS

```bash
cp .env.example .env
```

Windows PowerShell

```powershell
Copy-Item .env.example .env
```

The default configuration is:

```env
POSTGRES_DB=train_booking
POSTGRES_USER=train_admin
POSTGRES_PASSWORD=train_password

POSTGRES_PORT=5432

BACKEND_PORT=8080

FRONTEND_PORT=3000
```

---

# Start the System

Run a single command from the project root.

```bash
docker compose up --build
```

Docker Compose automatically performs the following:

1. Starts PostgreSQL.
2. Creates the database.
3. Restores the supplied database dump.
4. Waits until the database becomes healthy.
5. Builds the Spring Boot backend.
6. Starts the backend.
7. Builds the React application.
8. Starts Nginx.
9. Serves the frontend.

---

# Access the Application

After all containers have started successfully:

Frontend

```
http://localhost:3000
```

Backend API

```
http://localhost:8080
```

Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

# Stopping the Application

```bash
docker compose down
```

The PostgreSQL data volume is preserved.

---

# Resetting the Database

To completely remove the database and restore the original demonstration dataset:

```bash
docker compose down -v

docker compose up --build
```

---

# Docker Services

The system consists of three containers.

| Service       | Description                 |
| ------------- | --------------------------- |
| PostgreSQL    | Stores application data     |
| Spring Boot   | REST API and business logic |
| React + Nginx | Frontend application        |

---

# Database Initialization

The repository contains a PostgreSQL database dump containing sample data.

During the first execution, PostgreSQL automatically restores the supplied database, allowing the application to be used immediately without manually creating stations, routes, trains, coaches, schedules, or bookings.

The database is restored only when the PostgreSQL data directory is empty.

---

# Default Application Workflow

### Passenger

1. Search train schedules.
2. Select a train.
3. View available seats.
4. Select one or more seats.
5. Complete the booking.
6. Receive an order reference.
7. Retrieve booking details using the order reference.

---

### Administrator

1. View dashboard summary.
2. Manage stations.
3. Manage routes.
4. Configure route stations.
5. Create trains.
6. Add coaches.
7. Manage train schedules.
8. Edit timetable.
9. Monitor bookings.
10. View revenue and occupancy statistics.

---

# API Documentation

The backend exposes a complete REST API documented using Swagger.

Swagger UI can be accessed at:

```
http://localhost:8080/swagger-ui.html
```

---

# Notes

- Seat booking uses pessimistic locking to prevent concurrent booking conflicts.
- Multiple seats are booked atomically within a single database transaction.
- The application supports segment-based seat reuse, improving reserved seat utilization compared to conventional railway reservation systems.
- The system has been designed with configurable routes, stations, coaches, and schedules, allowing future railway extensions without code changes.
- If you dont see any train shcedules for the date you entered try creating new schedule or update existing one as admin using admin panel.
  -Sequerity features such as authentication and authorization are yet to implement.
