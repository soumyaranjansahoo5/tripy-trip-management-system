# 🧳 Tripy - Online Trip Management System

A full-stack **Java Spring Boot** REST API project with **MySQL**, **JWT Authentication**, and **Transaction Management**.

---

## 🛠️ Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Language     | Java 17                             |
| Framework    | Spring Boot 3.2.0                   |
| Security     | Spring Security + JWT               |
| Database     | MySQL 8.x                           |
| ORM          | Spring Data JPA / Hibernate         |
| Build Tool   | Maven                               |
| IDE          | Eclipse (Maven Project)             |

---

## 📁 Project Structure

```
tripy/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/tripy/
│   │   │   ├── TripyApplication.java          ← Main entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java        ← JWT Security config
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java        ← Register / Login
│   │   │   │   ├── TripController.java        ← Trip CRUD APIs
│   │   │   │   ├── BookingController.java     ← Booking APIs
│   │   │   │   └── AdminController.java       ← Admin only APIs
│   │   │   ├── dto/
│   │   │   │   └── TripyDTOs.java             ← All Request/Response DTOs
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Trip.java
│   │   │   │   └── Booking.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── TripRepository.java
│   │   │   │   └── BookingRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtUtils.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   └── service/
│   │   │       ├── AuthService.java
│   │   │       ├── TripService.java           ← @Transactional
│   │   │       └── BookingService.java        ← Full transaction rollback
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/tripy/
│           └── TripyApplicationTests.java
```

---

## ⚙️ Setup Instructions (Eclipse)

### Step 1 — Prerequisites
- Java 17+ installed → check: `java -version`
- MySQL 8.x installed and running
- Eclipse IDE with Maven support (Eclipse IDE for Enterprise Java Developers)
- Maven installed → check: `mvn -version`

### Step 2 — MySQL Database Setup
Open MySQL Workbench or MySQL CLI and run:

```sql
CREATE DATABASE tripy_db;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON tripy_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

> ⚠️ If your MySQL password is different, update `application.properties`:
> `spring.datasource.password=YOUR_PASSWORD`

### Step 3 — Import into Eclipse
1. Open Eclipse
2. Go to **File → Import → Maven → Existing Maven Projects**
3. Browse to the `tripy/` folder
4. Click **Finish**
5. Eclipse will auto-download all dependencies (wait for Maven build)

### Step 4 — Run the Project
1. Open `TripyApplication.java`
2. Right-click → **Run As → Java Application**
3. Console will show:
```
========================================
  Tripy - Trip Management System
  Running on: http://localhost:8080
========================================
```

> Tables will be auto-created in MySQL via `spring.jpa.hibernate.ddl-auto=update`

---

## 🔑 JWT Authentication Flow

```
1. Register  → POST /api/auth/register  → get token
2. Login     → POST /api/auth/login     → get token
3. All other requests → Add header:
   Authorization: Bearer <your_token>
```

---

## 📡 API Endpoints

### 🔓 Auth (No token needed)

| Method | URL                    | Description     |
|--------|------------------------|-----------------|
| POST   | /api/auth/register     | Register user   |
| POST   | /api/auth/login        | Login + get JWT |

### ✈️ Trips

| Method | URL                    | Auth     | Description              |
|--------|------------------------|----------|--------------------------|
| GET    | /api/trips             | Required | Get all active trips     |
| GET    | /api/trips/{id}        | Public   | Get trip by ID           |
| GET    | /api/trips/search      | Public   | Search trips             |
| POST   | /api/trips             | ADMIN    | Create new trip          |
| PUT    | /api/trips/{id}        | ADMIN    | Update trip              |
| DELETE | /api/trips/{id}        | ADMIN    | Cancel trip              |

### 📋 Bookings

| Method | URL                      | Auth     | Description              |
|--------|--------------------------|----------|--------------------------|
| POST   | /api/bookings            | Required | Book a trip              |
| GET    | /api/bookings/my         | Required | My bookings              |
| GET    | /api/bookings/{id}       | Required | Get booking by ID        |
| PUT    | /api/bookings/{id}/cancel| Required | Cancel booking           |

### 🛡️ Admin

| Method | URL                   | Auth  | Description        |
|--------|-----------------------|-------|--------------------|
| GET    | /api/admin/trips      | ADMIN | All trips          |
| GET    | /api/admin/bookings   | ADMIN | All bookings       |

---

## 🧪 Postman Testing Guide

### 1. Register a User
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Soumya",
  "email": "soumya@gmail.com",
  "password": "soumya123",
  "phone": "9668177321"
}
```

### 2. Login
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "soumya@gmail.com",
  "password": "soumya123"
}
```
→ Copy the `token` from the response.

### 3. Add Token to All Further Requests
In Postman → Headers tab:
```
Key:   Authorization
Value: Bearer eyJhbGciOi...  (paste your token)
```

### 4. Create a Trip (login as ADMIN first)
To make a user ADMIN, run this SQL:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'soumya@gmail.com';
```
Then login again to get a fresh token, and:
```
POST http://localhost:8080/api/trips
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Goa Beach Tour",
  "description": "Enjoy the beaches of Goa",
  "source": "Mumbai",
  "destination": "Goa",
  "startDate": "2025-01-15",
  "endDate": "2025-01-20",
  "pricePerPerson": 8500.00,
  "availableSeats": 20,
  "imageUrl": "https://example.com/goa.jpg"
}
```

### 5. Search Trips
```
GET http://localhost:8080/api/trips/search?source=Mumbai&destination=Goa&date=2025-01-01
```

### 6. Book a Trip
```
POST http://localhost:8080/api/bookings
Authorization: Bearer <token>
Content-Type: application/json

{
  "tripId": 1,
  "numberOfPersons": 2,
  "specialRequests": "Window seat preferred"
}
```

### 7. Cancel a Booking
```
PUT http://localhost:8080/api/bookings/1/cancel
Authorization: Bearer <token>
```

---

## 🔄 Transaction Management

The project uses Spring's `@Transactional` for data integrity:

- **Book a trip** → decreases seats AND creates booking in one atomic transaction. If either fails → full rollback.
- **Cancel a booking** → restores seats AND updates status atomically.
- **Read operations** → use `@Transactional(readOnly = true)` for performance.

---

## ❓ Troubleshooting

| Problem | Fix |
|---------|-----|
| Port 8080 in use | Change `server.port=8081` in application.properties |
| MySQL connection refused | Make sure MySQL service is running |
| Access denied (403) | Make sure you're sending `Authorization: Bearer <token>` |
| Token expired | Login again to get a new token |
| Tables not created | Check MySQL credentials in application.properties |
