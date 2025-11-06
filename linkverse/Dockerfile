# Use a standard, lightweight Java 17 image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file (which Render builds) into the container
# The 'target/*.jar' path is relative to the Root Directory ('linkverse')
COPY target/linkverse-0.0.1-SNAPSHOT.jar app.jar

# Tell Render what port the app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]