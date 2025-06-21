# Indus Sahulat Backend

A comprehensive ambulance service management system backend for Indus Hospital, designed to coordinate emergency medical
services between patients, ambulance providers, drivers, and hospitals.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Overview](#api-overview)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction

Indus Sahulat Backend is a Spring Boot-based REST API that powers an emergency ambulance service coordination platform.
The system facilitates real-time communication and coordination between patients in need of emergency medical transport,
ambulance service providers, drivers, and receiving hospitals.

The platform serves multiple user types:

- **Patients**: Can request emergency ambulance services and track their status
- **Ambulance Providers**: Manage their fleet of ambulances and drivers
- **Ambulance Drivers**: Receive assignments and update event status
- **Hospital Administrators**: Manage hospital information and receive patient admission requests

## Features

### Core Functionality

- **Multi-role Authentication**: Secure JWT-based authentication for different user types
- **Real-time Event Management**: Track ambulance requests from creation to completion
- **Live Location Tracking**: Monitor ambulance and driver locations in real-time
- **Hospital Coordination**: Manage patient admission requests and hospital assignments
- **Questionnaire System**: Medical assessment forms for emergency categorization
- **Notification System**: Real-time notifications via WebSocket and push notifications
- **Chat System**: In-app messaging between different user types

### User Management

- Patient registration and profile management
- Ambulance driver and provider onboarding
- Hospital administrator management
- Role-based access control

### Emergency Response

- Emergency event creation and categorization
- Ambulance assignment and dispatch
- Real-time status updates
- Hospital admission coordination
- Event state management with workflow transitions

## Tech Stack

### Backend Framework

- **Spring Boot 3.4.3**: Main application framework
- **Java 21**: Programming language
- **Maven**: Build tool and dependency management

### Database & Caching

- **PostgreSQL**: Primary relational database
- **Redis**: Caching and session management
- **JPA/Hibernate**: Object-relational mapping

### Security & Authentication

- **Spring Security**: Authentication and authorization
- **JWT (JSON Web Tokens)**: Stateless authentication
- **BCrypt**: Password hashing

### Real-time Communication

- **WebSocket**: Real-time bidirectional communication
- **STOMP**: Messaging protocol over WebSocket

### Additional Libraries

- **Lombok**: Reduces boilerplate code
- **MapStruct**: Object mapping
- **Jackson**: JSON processing
- **Caffeine**: In-memory caching
- **Validation**: Input validation framework

## Project Structure

```
Indus-sahulat-backend/
├── src/
│   ├── main/
│   │   ├── java/com/aktic/indussahulatbackend/
│   │   │   ├── config/                    # Application configuration classes
│   │   │   │   ├── ApplicationConfig.java # Main application configuration
│   │   │   │   ├── SecurityConfig.java    # Security and authentication setup
│   │   │   │   ├── WebSocketConfig.java   # WebSocket configuration
│   │   │   │   └── RedisConfig.java       # Redis connection setup
│   │   │   ├── controller/                # REST API endpoints
│   │   │   │   ├── patient/               # Patient-related endpoints
│   │   │   │   ├── ambulanceProvider/     # Ambulance provider endpoints
│   │   │   │   ├── ambulanceDriver/       # Driver-related endpoints
│   │   │   │   ├── hospitalAdmin/         # Hospital admin endpoints
│   │   │   │   ├── chat/                  # Chat functionality
│   │   │   │   └── notification/          # Notification endpoints
│   │   │   ├── service/                   # Business logic layer
│   │   │   │   ├── auth/                  # Authentication services
│   │   │   │   ├── ambulance/             # Ambulance management
│   │   │   │   ├── incidentEvent/         # Emergency event handling
│   │   │   │   ├── hospital/              # Hospital coordination
│   │   │   │   ├── chat/                  # Chat services
│   │   │   │   └── notification/          # Notification services
│   │   │   ├── repository/                # Data access layer
│   │   │   │   ├── patient/               # Patient data operations
│   │   │   │   ├── ambulance/             # Ambulance data operations
│   │   │   │   ├── hospital/              # Hospital data operations
│   │   │   │   └── redis/                 # Redis data operations
│   │   │   ├── model/                     # Data models and entities
│   │   │   │   ├── entity/                # Database entities
│   │   │   │   ├── request/               # API request DTOs
│   │   │   │   ├── response/              # API response DTOs
│   │   │   │   └── enums/                 # Enumeration types
│   │   │   ├── security/                  # Security-related classes
│   │   │   ├── filter/                    # HTTP request filters
│   │   │   ├── exception/                 # Exception handling
│   │   │   └── util/                      # Utility classes
│   │   └── resources/                     # Configuration files
│   │       ├── application.yml            # Main configuration
│   │       ├── application-dev.yml        # Development environment config
│   │       └── application-prod.yml       # Production environment config
│   └── test/                              # Test files
├── pom.xml                                # Maven project configuration
├── mvnw                                   # Maven wrapper script
└── README.md                              # This file
```

### Key Components Explained

- **Controllers**: Handle HTTP requests and define API endpoints for different user types
- **Services**: Contain business logic for ambulance assignment, event management, and user operations
- **Repositories**: Data access layer for database operations using Spring Data JPA
- **Models**: Define data structures including entities, DTOs, and enums
- **Config**: Application configuration including security, WebSocket, and database setup
- **Security**: JWT authentication, user details service, and security filters

## Installation

### Prerequisites

- **Java 21** or higher
- **Maven 3.6** or higher
- **PostgreSQL 12** or higher
- **Redis 6** or higher

### Step-by-Step Setup

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd Indus-sahulat-backend
   ```

2. **Set up PostgreSQL database**

   ```sql
   CREATE DATABASE IndusSahulat;
   CREATE USER postgres WITH PASSWORD '123456789';
   GRANT ALL PRIVILEGES ON DATABASE IndusSahulat TO postgres;
   ```

3. **Configure Redis**

   ```bash
   # Start Redis server (default port 6379)
   redis-server
   ```

4. **Set environment variables**

   ```bash
   export JWT_SECRET_KEY="your-secret-key-here"
   export SUPABASE_PASSWORD="your-database-password"
   ```

5. **Build the project**

   ```bash
   mvn clean install
   ```

6. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Configuration

### Environment Variables

| Variable            | Description                         | Default           |
|---------------------|-------------------------------------|-------------------|
| `JWT_SECRET_KEY`    | Secret key for JWT token generation | Required          |
| `SUPABASE_PASSWORD` | Database password for production    | Required for prod |

### Database Configuration

The application uses different configurations for development and production:

- **Development**: Local PostgreSQL instance
- **Production**: Supabase PostgreSQL (cloud-hosted)

### Redis Configuration

- **Host**: localhost (dev) / redis-server (prod)
- **Port**: 6379
- **Timeout**: 600000ms

## Usage

### Starting the Application

```bash
# Development mode
mvn spring-boot:run -Dspring.profiles.active=dev

# Production mode
mvn spring-boot:run -Dspring.profiles.active=prod
```

### API Endpoints

The application provides RESTful APIs organized by user type:

- **Patient APIs**: `/api/patient/*`
- **Ambulance Provider APIs**: `/api/ambulance-provider/*`
- **Ambulance Driver APIs**: `/api/ambulance-driver/*`
- **Hospital Admin APIs**: `/api/hospital-admin/*`

### WebSocket Connections

Real-time updates are available via WebSocket connections:

- **Event Updates**: `/ws/events`
- **Chat Messages**: `/ws/chat`
- **Notifications**: `/ws/notifications`

## API Overview

### Authentication Flow

1. **Registration**: Users register with role-specific endpoints
2. **Login**: JWT token generation upon successful authentication
3. **Authorization**: Role-based access control for all endpoints

### Event Management Flow

1. **Event Creation**: Patient creates emergency event with questionnaire
2. **Ambulance Assignment**: Provider assigns ambulance and driver
3. **Driver Acceptance**: Driver accepts or rejects assignment
4. **Hospital Coordination**: Driver selects hospital and sends admission request
5. **Event Completion**: Patient admitted to hospital

### Real-time Features

- **Live Location Updates**: Ambulance and driver location tracking
- **Status Notifications**: Real-time event status updates
- **Chat System**: In-app messaging between users
- **Push Notifications**: Instant alerts for important events

## Architecture

### System Architecture

The application follows a layered architecture pattern:

```
┌─────────────────────────────────────┐
│           Controllers               │  ← REST API endpoints
├─────────────────────────────────────┤
│            Services                 │  ← Business logic
├─────────────────────────────────────┤
│          Repositories               │  ← Data access
├─────────────────────────────────────┤
│         Database Layer              │  ← PostgreSQL + Redis
└─────────────────────────────────────┘
```

### Key Design Patterns

- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic encapsulation
- **DTO Pattern**: Data transfer objects for API communication
- **State Pattern**: Event state management
- **Observer Pattern**: Real-time notifications

### Security Architecture

- **JWT Authentication**: Stateless token-based authentication
- **Role-based Access Control**: Different permissions per user type
- **Request Filtering**: JWT validation on protected endpoints
- **Password Encryption**: BCrypt hashing for secure storage

## Contributing

We welcome contributions to improve the Indus Sahulat Backend. Please follow these guidelines:

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Make your changes following the existing code style
4. Add tests for new functionality
5. Ensure all tests pass: `mvn test`
6. Commit your changes: `git commit -m 'Add feature description'`
7. Push to the branch: `git push origin feature/your-feature-name`
8. Submit a pull request

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex business logic
- Maintain consistent indentation and formatting
- Write unit tests for new services and repositories

### Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

### Pull Request Process

1. Ensure your code follows the project's coding standards
2. Update documentation if needed
3. Include relevant tests
4. Provide a clear description of changes
5. Reference any related issues

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

### Project Maintainer

- **Author**: Azizullah Khan
- **Email**: aziz.bin.aman16@gmail.com

### Support

For technical support or questions:

- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation for common solutions

### Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL and Redis communities
- All contributors and testers

---

**Note**: This is a production-ready ambulance service management system. Please ensure proper security measures and
testing before deployment in a healthcare environment.
