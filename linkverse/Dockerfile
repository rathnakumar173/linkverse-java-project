# Use a different, highly-compatible Java 17 image
FROM eclipse-temurin:17-jdk-focal

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file (which Render builds) into the container
COPY target/linkverse-0.0.1-SNAPSHOT.jar app.jar

# Tell Render what port the app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]