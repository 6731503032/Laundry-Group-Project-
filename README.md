# Laundry Booking SystemTO DO THIS WEEK(LOGIN PAGE):



Spring Boot backend with role-based user authentication, machine booking, and rating system.LOGIN PAGE WILL HAVE 2 WAYS TO LOGIN DIVIDED BY ROLE IN USER.java(manager and student)

VALIDATE IF THE USER IS CORRECT WITH THE LOGIN FORM VALIDATE BY name and password in USER.java which info found in user.sql

## Development Planning

1.IF VALIDATION SUCCESSFUL, direct to booking page

### TO DO THIS WEEK (LOGIN PAGE)2. IF NOT SUCCESSFUL, give error



**Login Page Requirements:**THERE IS ALSO CREATE ACCOUNT PAGE with the button in login page,

- LOGIN PAGE WILL HAVE 2 WAYS TO LOGIN DIVIDED BY ROLE IN USER.java (manager and student)THINGS TO BE ASKED FOR CREATION found in user.java:  

- VALIDATE IF THE USER IS CORRECT WITH THE LOGIN FORM VALIDATE BY name and password in USER.java which info found in user.sql    private String studentId;

    

**Login Flow:**    @Column(nullable = false)

1. IF VALIDATION SUCCESSFUL, direct to booking page    private String name;

2. IF NOT SUCCESSFUL, give error    

    @Column(nullable = false, unique = true)

**Create Account Page:**    private String email;

- THERE IS ALSO CREATE ACCOUNT PAGE with the button in login page    

- THINGS TO BE ASKED FOR CREATION found in user.java:    @Column(nullable = false)

  - `private String studentId;`    private String password;

  - `@Column(nullable = false) private String name;`    

  - `@Column(nullable = false, unique = true) private String email;`    @Enumerated(EnumType.STRING)

  - `@Column(nullable = false) private String password;`    private Role role;

  - `@Enumerated(EnumType.STRING) private Role role;`



---



### THINGS TO WORK ON



**Customer Interface:**

- Create new accountTHINGS TO WORK ON: 

- Login

- Front page (Time-slot)Customer 

- Book slot page (which Machine)Create new account

- Timer page after confirmLogin

- Completion PageFront page(Time-slot)

- End Service Page (rating system)Book slot page(which Machine) 

- Machine details PageTimer page after confirm

Completion Page

**Admin Interface:**End​ Service Page(rating system) 

- The money is givenMachine details Page

- Editing Page for Washing Machine

- Editing Page for Students booked

- Confirm PageAdmin

The money is given

**Database:**Editing Page for Washing Machine

- BookingEditing Page for Students booked

- Payment Confirmed StudentsConfirm Page

- Rating

- Registered Student

Database : 

**Frontend Coding:**Booking

- AJAX / THYMELEAF / REACTJSPayment Confirmed Students

Rating

---Registered Student



## Quick Start



### Build & Run BackendFRONTEND CODING:

```bashAJAX/THYMELEAF/REACTJS

cd backend

mvn clean install# Laundry Group — backend 

mvn spring-boot:run

```Key points

Backend runs on `http://localhost:8080`- Login accepts either `studentId` OR `email` plus a `password`.

- Passwords are BCrypt-hashed on register (via `PasswordEncoder`) and verified on login.

### Build Frontend

```bashQuick start — build and run

cd frontend

# Configure CORS settings in backend application.properties```bash

```cd backend

# build jar

---mvn -DskipTests package



## Testing# run (development)

mvn -DskipTests spring-boot:run

### Run All Tests

```bash# or run the packaged jar

mvn -f backend/pom.xml testjava -jar target/backend-1.0-SNAPSHOT.jar

``````



### Run Specific Test ClassThe server listens on port 8080 by default. Watch the console for Spring Boot startup lines (Hibernate DDL, "Started", Tomcat port).

```bash

mvn -f backend/pom.xml test -Dtest=AuthControllerTestQuick functional tests (use a second terminal)

```

- Register a user

### Run Single Test Method```bash

```bashcurl -i -X POST http://localhost:8080/api/auth/register \

mvn -f backend/pom.xml test -Dtest=AuthControllerTest#testLoginSuccess_WithStudentId    -H "Content-Type: application/json" \

```    -d '{"studentId":"S123","name":"Test User","email":"test@example.com","password":"secret","role":"STUDENT"}'

```

### View Test Results

```bash- Login by studentId

# After running tests, view results```bash

cat backend/target/surefire-reports/AuthControllerTest.txtcurl -i -X POST http://localhost:8080/api/auth/login \

```    -H "Content-Type: application/json" \

    -d '{"studentId":"S123","password":"secret"}'

### Test Output```

```

Tests run: 6, Failures: 0, Errors: 0, Skipped: 0- Login by email

BUILD SUCCESS```bash

```curl -i -X POST http://localhost:8080/api/auth/login \

    -H "Content-Type: application/json" \

---    -d '{"email":"test@example.com","password":"secret"}'

```

## API Endpoints

Expected responses

### Authentication (`/api/auth`)- Register: HTTP 200 with JSON {"status":"ok","id":...}

| Method | Endpoint | Description |- Login: HTTP 200 with JSON {"status":"ok","userId":..., "studentId":..., "name":..., "role":...}

|--------|----------|-------------|

| POST | `/api/auth/login` | Login with studentId/email + password |Notes for developers

| POST | `/api/auth/register` | Create new account |- If you see 401 with `WWW-Authenticate: Basic realm="Realm"`, Spring Security is protecting the endpoint — restart the server after adding `SecurityConfig` (we included a permissive config for `/api/auth/**`).

| GET | `/api/auth/users` | List all users |- If startup fails because of SQL inserts (application tries to run `user.sql` before tables exist), the project is configured to let Hibernate update the schema (`spring.jpa.hibernate.ddl-auto=update`) and defer datasource initialization.

- A temporary debug endpoint exists at `GET /api/auth/debug/users` (returns users without passwords) to help verify registration; remove it before production.

### Login Example

**Request:**

```json```

{

  "studentId": "S12345",Quick tests (use a separate terminal while the server is running):

  "password": "password123"

}
```

**Success Response (200):**
```json
{
  "id": 1,
  "studentId": "S12345",
  "name": "John Doe",
  "role": "STUDENT"
}
```

**Error Response (401):**
```json
{
  "error": "Invalid credentials"
}
```

---

## Test Suite: AuthControllerTest

**Status:** ✅ All 6 tests passing

| Test | Scenario | Expected |
|------|----------|----------|
| `testLoginSuccess_WithStudentId` | Valid studentId login | 200 OK |
| `testLoginSuccess_WithEmail` | Valid email login | 200 OK |
| `testLoginFailure_InvalidCredentials` | Wrong password | 401 Unauthorized |
| `testLoginValidation_MissingStudentIdAndEmail` | No ID/email | 400 Bad Request |
| `testLoginValidation_MissingPassword` | No password | 400 Bad Request |
| `testLoginValidation_NullPassword` | Null password | 400 Bad Request |

---

## Project Structure

```
backend/src/main/java/
├── App.java                    # Main application
├── AuthController.java         # Login/auth endpoints
├── MachineController.java      # Machine endpoints
├── UserController.java         # User management
├── ViewController.java         # View endpoints
├── config/                     # Spring config
├── model/                      # Entities (User, Machine, Rating)
├── repo/                       # Data repositories
└── service/                    # Business logic

backend/src/test/java/
└── AuthControllerTest.java     # 6 login tests
```

---

## Technology Stack

- **Backend:** Spring Boot 2.7.14
- **Database:** H2 (in-memory)
- **Security:** Spring Security + BCrypt
- **Testing:** JUnit 5 + MockMvc + Mockito
- **Build:** Maven

---

## Features

✅ User authentication (studentId or email)  
✅ Role-based access (STUDENT, MANAGER)  
✅ BCrypt password encryption  
✅ 6 comprehensive login tests  
✅ CORS-enabled API  
✅ H2 database with JPA  

---

## Troubleshooting

**Tests failing?**
```bash
mvn -f backend/pom.xml clean test
```

**Port in use?**
```bash
# Change in application.properties
server.port=8081
```
