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
CREATE DATABASE rentalDb;
USE rentalDb;
```

Import the SQL schema located in src/main/resources/script.sql:

```
SOURCE src/main/resources/script.sql;
```

Create a new user and grant all privileges on the created database

```
CREATE USER 'rentaluser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON rentalDb.* TO 'rentaluser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure the application

Copy the sample environment configuration file :

```
cp .env.example .env
```

Update env variables to match with your environment: 

```
DATABASE_URL=jdbc:mysql://localhost:3306/rentalDb
DATABASE_USERNAME=rentaluser
DATABASE_PASSWORD=your_password
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

## Additional Notes

Testing: To run the unit tests included with the application, use mvn test.
Security Configurations: If you have specific security requirements, consider updating src/main/resources/application-security.properties.
Database Backups: Regularly backup your database using MySQL dump tools or similar.

By following these instructions, you should have a fully functional local setup of the Rental Service API, ready for development and testing purposes.