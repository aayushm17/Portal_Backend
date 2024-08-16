# Spring Boot Certificate Management Backend

This is a Spring Boot application that provides APIs for certificate management, including filtering, revoking, and downloading certificates. The application includes features for role-based access control, JWT authentication, and data filtering.

## Prerequisites

Before you begin, ensure you have the following installed:

Prerequisites
Java Development Kit (JDK) 17
Ensure that JDK 17 or higher is installed. You can check your Java version by running java -version in your terminal.

Apache Maven 3.6+
Maven is required to manage the project's build lifecycle and dependencies.

PostgreSQL Database
PostgreSQL must be installed and running, with appropriate configuration (username, password, and database) as required by your application.

Git (Optional)
Git is recommended for version control.


## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/your-repository-name.git
   cd your-repository-name
2. Build the project using Maven:


	mvn clean install

Application Properties
Configure your application properties in src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.maximum-pool-size=50
server.port=8082


3. Run the application using Maven:
	mvn spring-boot:run

The backend server will start at http://localhost:8080.
