# --- STAGE 1: Build The Project ---
# Use a Maven image to build the app
FROM maven:3.8-jdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the JAR
COPY src ./src
RUN mvn clean install


# --- STAGE 2: Create the Final Run Image ---
# Use the lightweight Java 17 image
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# Copy ONLY the built JAR file from the 'build' stage
COPY --from=build /app/target/linkverse-0.0.1-SNAPSHOT.jar app.jar

# Tell Render what port the app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]