# Secure Account Management API

#### Welcome to the Secure Account Management API, a robust and secure REST API designed for comprehensive account management functionalities. This API ensures top-notch security measures while offering seamless account creation, login, password recovery, and user data management.

## Description

#### The Secure Account Management API is built on Java with Spring Boot, leveraging Spring Security for robust authentication and authorization mechanisms. It integrates with PostgreSQL for efficient database management, ensuring reliable storage of sensitive user information.


## Key Features/Functionalities

#### * Account Creation/Signup: Allows users to create accounts with email verification, including features for resending verification codes.

#### * Login/Signin: Implements advanced suspicious login detection, two-factor authentication, and JWT-based authentication and authorization.

#### * Forgot Password: Offers a secure password reset mechanism through email verification.

#### * Update: Enables users to modify security preferences and account details securely.

#### * View Data: Ensures robust authentication and authorization mechanisms through JWT, securing sensitive data access.
  
#### * Multi-Factor Authentication (MFA): Implements an additional layer of security for user authentication.


## Security Focus


#### * Email Verification: Ensures account authenticity by requiring email verification during signup and password reset processes.

#### * Two-Factor Authentication (2FA): Adds an extra layer of security by implementing 2FA for user logins.

#### * Secure Data Storage: Leverages PostgreSQL for safe and secure storage of sensitive user information.


## ERD

![Screenshot 2023-12-14 at 19 24 10](https://github.com/TresorKL/SecureAccountManagement-API/assets/86111190/5161cf6a-db05-4e8c-81ab-15e312a61664)

## Tech Stack

#### * Java: Utilizes Spring Boot with Spring Security for robust API development.

#### * PostgreSQL: Integrates with a PostgreSQL database for efficient data management.

#### * OAuth, JWT: Implements OAuth for secure authorization and JWT for authentication.
