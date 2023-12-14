Welcome to the Secure Account Management API, a robust and secure REST API designed for comprehensive account management functionalities. This API ensures top-notch security measures while offering seamless account creation, login, password recovery, and user data management.

Description
The Secure Account Management API is built on Java with Spring Boot, leveraging Spring Security for robust authentication and authorization mechanisms. It integrates with PostgreSQL for efficient database management, ensuring reliable storage of sensitive user information.

Key Features/Functionalities
Account Creation/Signup: Allows users to create accounts with email verification, including features for resending verification codes.
Login/Signin: Implements advanced suspicious login detection, two-factor authentication, and JWT-based authentication and authorization.
Forgot Password: Offers a secure password reset mechanism through email verification.
Update: Enables users to modify security preferences and account details securely.
View Data: Ensures robust authentication and authorization mechanisms through JWT, securing sensitive data access.
Multi-Factor Authentication (MFA): Implements an additional layer of security for user authentication.
Security Focus
Email Verification: Ensures account authenticity by requiring email verification during signup and password reset processes.
Suspicious Login Detection: Monitors login activities for irregularities such as unusual locations, multiple failed attempts, and device identification.
Two-Factor Authentication (2FA): Adds an extra layer of security by implementing 2FA for user logins.
SSL/TLS Implementation: Secures data transmission by utilizing SSL/TLS protocols.
Input Validation: Enforces strict input validation to mitigate common security vulnerabilities.
Secure Data Storage: Leverages PostgreSQL for safe and secure storage of sensitive user information.
Tech Stack
Java: Utilizes Spring Boot with Spring Security for robust API development.
PostgreSQL: Integrates with a PostgreSQL database for efficient data management.
OAuth, JWT: Implements OAuth for secure authorization and JWT for authentication.
Installation and Setup
Clone the repository.
Configure the PostgreSQL database.
Set up environment variables and configurations.
Build and run the application using Maven or your preferred method.
Usage
Please refer to the API documentation for detailed information on endpoints, request/response formats, and authentication methods.

Contributors
[Your Name]
[Contributor 1]
[Contributor 2]
Support
For any inquiries or issues, please contact [Your Email].
