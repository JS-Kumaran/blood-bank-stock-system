# Blood Bank Stock System

## Project Overview

The Blood Bank Stock System is a Spring Boot backend application designed to manage blood bank inventory. It tracks blood stock by blood group, records donations, processes blood requests, and maintains a transaction history of all stock movements.

## Objective

This application provides a robust API for blood banks to:

- Track current blood stock levels for all blood groups
- Record blood donations (IN transactions)
- Create and process blood requests (OUT transactions)
- Ensure stock never becomes negative
- Maintain a complete audit trail of all stock movements

## Features

- **Donation Management**: Record blood donations with automatic stock updates
- **Request Processing**: Create and fulfill blood requests
- **Stock Management**: Real-time inventory tracking with pessimistic locking
- **Transaction History**: Complete audit trail of all IN/OUT transactions
- **Validation**: Comprehensive input validation and business rule enforcement
- **Exception Handling**: Centralized error handling with consistent responses

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.3**
- **Maven** for build automation
- **Spring Data JPA** for data persistence
- **MySQL** as the relational database
- **Jakarta Bean Validation** for input validation
- **REST APIs** for all operations
- **Postman** for API testing
- **Git/GitHub** for version control

## Architecture

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic and transaction management
- **Repositories**: Data access layer using Spring Data JPA
- **Entities**: JPA entities mapping to database tables
