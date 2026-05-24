# 🩺 KronSoft Project


![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![Kotlin](https://img.shields.io/badge/Kotlin-Android-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=jetpackcompose)
![Firebase](https://img.shields.io/badge/Firebase-Auth-yellow?style=for-the-badge&logo=firebase)

**KronSoft Project** is a medical management application designed to improve the communication between **patients** and **doctors** 

The application allows patients to complete daily medical questionnaires, doctors to monitor patient evolution, and receptionists to manage users inside the system.

---

## ✨ Main Features

- Role-based authentication
- Doctor dashboard
- Patient dashboard
- Medical questionnaires
- Patient evolution reports
- AI-generated medical summaries
- Chat between users
- Receptionist module for adding doctors and patients

---

## 👥 User Roles

| Role | Description |
|---|---|
| **Doctor** | Views patients, reports and medical evolution |
| **Patient** | Completes questionnaires and views medical information |
| **Receptionist** | Adds and manages doctors and patients |

---

## 📱 Application Flow

1. The user logs into the Android application.
2. The system identifies the user role.
3. The user is redirected to the correct dashboard:
   - Doctor Dashboard
   - Patient Dashboard
   - Receptionist Dashboard
4. The frontend communicates with the backend through REST API calls.
5. Medical data, questionnaires and reports are stored and processed by the backend.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/larisaandreea212/KronSoft-Project.git
cd KronSoft-Project
```

---

## ⚙️ Backend Setup

```bash
cd backend
```

Configure the PostgreSQL database connection in the application properties file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/health_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Run the backend:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

---

## 📲 Frontend Setup

Open the `frontend` folder in **Android Studio**.

Make sure the Firebase configuration file exists:

```text
frontend/app/google-services.json
```

Then run the application on an emulator or a physical Android device.

For Android Emulator, the backend URL should usually be:

```text
http://10.0.2.2:8080
```

instead of:

```text
http://localhost:8080
```

---

## 📌 Project Modules

```text
backend/
├── controller/    # REST controllers
├── dto/           # Data transfer objects
├── entity/        # Database entities
├── repository/    # JPA repositories
├── services/      # Business logic
└── config/        # App configuration
```

```text
frontend/
├── data/          # Models, API services and repositories
├── ui/            # Screens, components and theme
└── viewmodels/    # State and UI logic
```

---

## 🔮 Future Improvements

- Improve UI/UX design
- Add more advanced medical statistics
- Add unit and integration tests
- Add Docker support
- Improve role-based authorization
- Add complete deployment instructions
