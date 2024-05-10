# Rental Service API

This project is a Spring Boot application for a rental service.

## Prerequisites

Before you begin, ensure you have the following installed:
- Java JDK 17 or newer
- Maven 4.0 or newer
- MySQL Server 5.7 or newer

## Installation

Follow these steps to set up and run the project locally:

### 1. Clone the repository

Clone this repository to your local machine using the following command:

```
git clone https://github.com/tibrook/rental.git
cd rental
```

### 2. Set up the database

Create a new MySQL database and import the database script:

```
CREATE DATABASE rentaldb;
USE rentaldb;
-- Copy / paste the content of script.sql in src/main/resources
```

Create a new user and grant all privileges on the created database

### 3. Configure the application

Copy the sample environment configuration file and modify it to match your local setup:

```
cp .env.example .env
```
### 4. Build and run the application

```
mvn clean install
mvn spring-boot:run
```

## Usage
Once the application is running, you can access endpoints defined in the Swagger UI at:

```
http://localhost:8082/swagger-ui.html

```

This will provide you with an interactive API documentation where you can test out different endpoints.
