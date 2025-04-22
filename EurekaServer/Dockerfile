# Use a base image with Java 17+ for Spring Boot 3.4.x
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built jar into the container (adjust the JAR name accordingly)
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Eureka Server runs on (usually 8761)
EXPOSE 8761

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
