# Secure Account Management API

#### Welcome to the Secure Account Management API, a robust and secure REST API designed for comprehensive account management functionalities. This API ensures top-notch security measures while offering seamless account creation, login, password recovery, and user data management.

## Description

#### The Secure Account Management API is built on Java with Spring Boot, leveraging Spring Security for robust authentication and authorization mechanisms. It integrates with PostgreSQL for efficient database management, ensuring reliable storage of sensitive user information.


## Key Features/Functionalities

#### * Account Creation/Signup: Allows users to create accounts with email verification, including features for resending verification codes.

#### * Login/Signin: Implements two-factor authentication, and JWT-based authentication and authorization.

#### * Forgot Password: Offers a secure password reset mechanism through email verification.

#### * Update: Enables users to modify security preferences and account details securely.

#### * View Data: Ensures robust authentication and authorization mechanisms through JWT, securing sensitive data access.
  
#### * Multi-Factor Authentication (MFA): Implements an additional layer of security for user authentication.


## ADVANCED KEY FEATURE (suspicious login detection)

### Steps for Implementing ML/AI-based Suspicious Login Detection:

#### 1. Data Collection:
- Gather a comprehensive dataset containing historical login data, including:
  - IP addresses, geolocations, and timestamps of logins
  - User-agent information (browser, device, OS)
  - Session durations and patterns
  - Successful and failed login attempts
  - Account activity post-login

#### 2. Feature Engineering:
- Extract relevant features from the collected data, such as:
  - Geographical features (distance between login locations)
  - Time-based features (login times, time between logins)
  - Device-related features (type of device, changes in user-agent info)
  - Behavioral features (success rate, failed attempts, session duration)

#### 3. Model Development:
- Utilize machine learning algorithms or techniques:
  - **Anomaly Detection:** Train models (e.g., Isolation Forest, One-Class SVM, or Autoencoders) to identify anomalies or deviations from normal login behavior.
  - **Classification Models:** Employ supervised learning models (e.g., Random Forest, Gradient Boosting, Neural Networks) to classify logins as normal or suspicious based on learned patterns.

#### 4. Training and Validation:
- Split the dataset into training and validation sets.
- Train the ML models on the historical data, adjusting hyperparameters and optimizing the models for performance.

#### 5. Model Evaluation:
- Evaluate the models using validation data, assessing their accuracy, precision, recall, and F1-score.
- Fine-tune the models based on evaluation metrics to minimize false positives (flagging legitimate logins as suspicious) and false negatives (missing actual suspicious logins).

#### 6. Integration and Real-Time Detection:
- Integrate the trained model into your authentication workflow to analyze incoming login attempts in real-time.
- Apply the model to incoming login data and use the output to flag or block suspicious logins.

#### 7. Continuous Learning and Improvement:
- Regularly update and retrain the model using new data to adapt to evolving user behaviors and emerging threats.
- Implement feedback mechanisms to improve the model's accuracy over time.

Implementing ML/AI for suspicious login detection enables the system to learn from historical data and adapt to new patterns, enhancing the security of the authentication process by identifying potential threats more accurately.
















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
