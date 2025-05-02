# JoinAI Support Ticket System

## Overview
JoinAI Support Ticket System is a Spring Boot application designed to manage customer support tickets efficiently. The system automatically assigns tickets to support agents, tracks ticket status, sends email notifications, and provides performance statistics for agents and the overall system.

## Features
- **Automated Ticket Assignment**: Tickets are automatically assigned to the admin with the least workload
- **Email Notifications**: Automatic email notifications for ticket creation, updates, and closure
- **Ticket Status Tracking**: Track tickets through their lifecycle (OPEN, CLOSED, etc.)
- **Priority Management**: Automatic priority assignment for tickets
- **Performance Statistics**: 
  - Daily, weekly, and monthly ticket statistics for agents
  - Average resolution time for the entire system
- **Admin Dashboard**: View assigned tickets and performance metrics

## Technology Stack
- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL Database
- Spring Mail for email notifications
- Lombok for reducing boilerplate code
- JMeter and Gatling for performance testing

## Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Setup Instructions

### Database Setup
1. Install PostgreSQL if not already installed
2. Create a database named `spring`
3. Configure the database connection in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5433/spring
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

### Email Configuration
Configure email settings in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Building and Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   or
   ```bash
   java -jar target/TestAI-0.0.1-SNAPSHOT.jar
   ```

The application will start on port 8082 by default. You can change this in the `application.properties` file.

## API Endpoints

### Ticket Management
- **POST /ticket/launchTicket**: Create a new support ticket
- **POST /ticket/updateTicket**: Update ticket status
- **GET /ticket/getMyTickets**: Get tickets assigned to the authenticated admin
- **GET /ticket/ticketNotifications**: Get ticket notifications for a specific email

### Statistics
- **GET /ticket/getStats**: Get overall system statistics
- **GET /ticket/getMyStats**: Get statistics for the authenticated admin

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.2/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.2/reference/web/servlet.html)
* [Ollama](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

## Maven Parent overrides
Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
