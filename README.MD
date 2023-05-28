# Phone Management

This project is a Java application built using the following technology stack: Java 17, Spring Boot 3, Spring Data R2DBC, Flyway DB Migration, Spring WebFlux, JUnit 5, Mockito, MockWebServer, MySQL, and H2 in-memory DB for tests. It provides several features and integrations, as outlined below.

## Features Implemented

1. **Phone Management:**
    - Search, list, view, create, delete, and update phones.

2. **User Management:**
    - Search, list, view, create, delete, and update users.

3. **Borrowing System:**
    - Ability to borrow phones and track return dates.

4. **Integration with back4app:**
    - Integration with a third-party API (back4app) to retrieve device descriptions.

5. **Tests:**
    - Unit tests and integration tests have been implemented using JUnit 5, Mockito, and MockWebServer.

6. **Error Handling:**
    - Error handling mechanisms are in place to handle exceptions gracefully.

7. **Database Migration:**
    - Flyway DB Migration is used to manage database schema evolution.

8. **Swagger API Documentation:**
    - API endpoints are documented using Swagger for easy reference and testing.

## Features Yet to be Implemented

1. **Security:**
    - Authentication and authorization mechanisms are not yet implemented. It is recommended to introduce security measures to protect the application and its resources.

2. **CheckStyle:**
    - Code style checks are not yet configured. It is advised to integrate CheckStyle or a similar tool to ensure consistent code formatting and adherence to coding standards.

3. **Code Coverage:**
    - Code coverage analysis is not yet set up. It is recommended to introduce a code coverage tool (e.g., JaCoCo) to measure and improve code coverage during testing.

## Prerequisites

To run this project, ensure that you have the following installed:

- Java 17 Development Kit (JDK)
- MySQL database
- Gradle build tools

## Getting Started

Follow the steps below to set up and run the project:

1. Clone the repository:
   ```shell
   git clone https://github.com/Mhretaab/phone-management.git
   ```

2. Navigate to the project directory:
   ```shell
   cd phone-management
   ```

3. Update the database configuration:
    - Open the `application.properties` file located in the `src/main/resources` directory.
    - Update the database connection details according to your MySQL setup.
    - Update back4app API connection details

4. Build the project:

      ```shell
      gradle clean build
      ```

5. Run the application:

      ```shell
      gradle bootRun
      ```

6. Access the application:
    - Once the application starts successfully, you can access it at `http://localhost:8080`.
    - Use the provided Swagger API documentation to explore and interact with the available endpoints `http://localhost:8080/api/swagger-ui.html`.
    - Or use the postman collection provided in the doc directory under the project root directory

## Setting up MySQL DB Using Docker

Once you install Docker in your machine:
- Run docker command

     ```shell
       docker run --name phone-mysql --network=host -e MYSQL_ROOT_PASSWORD=password -d mysql:latest
     ```

- Or run the docker compose file located under project root directory

     ```shell
     docker-compose up -d
  ```

## Conclusion

This Java project provides essential functionalities for managing phones, users, and phone borrowing. It integrates with a third-party API for device descriptions and ensures proper error handling, database migration, and test coverage. However, it lacks security measures, code style checks, and code coverage analysis, which should be implemented as future enhancements.

If you have any questions or encounter any issues while using this project, please contact the project maintainers for support.

Thank you for

## NB: if MySQL db connection with password fails

```shell
      mysql -u root -h localhost -p
```
```shell
      ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
```
```shell
      FLUSH PRIVILEGES;
```

