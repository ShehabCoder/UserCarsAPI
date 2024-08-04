# 🚗 Java Car Search API

This SpringBoot application provides a robust solution for user registration, login, and searching users' related cars. Designed to handle a large dataset of user cars and car models efficiently.

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Setup](#setup)
3. [Running the Application](#running-the-application)
4. [API Details](#api-details)
5. [Data Models](#data-models)
6. [Tools Used](#tools-used)
7. [Testing](#testing)
8. [Troubleshooting](#troubleshooting)

## 🛠 Prerequisites

Ensure you have the following installed:
- Java Development Kit (JDK) 21 or later
- MongoDB Compass and Server
- Redis Server
- Postman (for API testing)

> ⚠️ **Important**: Make sure MongoDB and Redis servers are running before starting the application!

## 🚀 Setup

1. Clone the repository:
   ```
   git clone https://github.com/ShehabCoder/UserCarsAPI or see in mail the source code
   cd Jar&PostMan collections
   ```

2. Set up MongoDB:
   - Start MongoDB compas
   - The following collections will be created automatically:
     - `USER_CARS`
     - `CAR_MODELS`
     - `USERS` [when registering a User]

3. Set up Redis:
   - Start Redis server

4. Configure application properties:
   - Open `src/main/resources/application.properties`
   - Set MongoDB and Redis connection details if different from default

## 🏃‍♂️ Running the Application

You have two options to run the application:

1. Using the JAR file:
   ```
   - got to posman collection and import it in folder app/Jar&PostMan
   - run the app using this command  -> java -jar app-0.0.1-SNAPSHOT
   ```
   ```
   ⏳**Note** : for First Search on /api/search, you will see a Log Cache is Missing this because Redis [will obtain the data for the second search] 
   ```

2. Through your IDE:
   - Open the project in your preferred IDE
   - Run the main application class

> ⏳ **Note**: Wait approximately 5 minutes after starting the application. This time is needed to generate the User Cars Collection and Car Model in MongoDB.

## 🔌 API Details

### 1. User Registration API
- **Endpoint:** `POST /api/register`
- **Input:**
  ```json
  {
    "firstName": "string",
    "lastName": "string",
    "loginId": "string",
    "password": "string"
  }
  ```

### 2. Login API
- **Endpoint:** `POST /api/login`
- **Input:**
  ```json
  {
    "loginId": "string",
    "password": "string"
  }
  ```
- **Output:** Success message and a unique token for use in the search API

### 3. Search User Cars API
- **Endpoint:** `GET /api/search`
- **Input:** Query parameters (firstName, lastName, car plate number)
- **Authentication:** Required (use the token from login API)

## 💾 Data Models

Detailed information about `USER_CARS`, `CAR_MODELS`, and `USERS` collections.

## 🛠 Tools Used

- SpringBoot
- Spring Data
- MongoDB
- Redis
- Maven

## 🧪 Testing

A Postman collection is provided for easy testing of the endpoints. 

> 🔒 **Security Note**: When logging in with a new user, the previous token will be blacklisted through Redis. Always obtain a new token from `/login` before using `/search`.

## 🆘 Troubleshooting

If you encounter any issues or need clarification, please don't hesitate to contact the development team.

---

📬 For any questions or support, please reach out to [shehabcoder@gmail.com](mailto:shehabcoder@gmail.com)
