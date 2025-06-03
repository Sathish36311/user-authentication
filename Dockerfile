FROM openjdk:17-jdk-slim

# Copy and rename WAR file
COPY target/learning-0.0.1-SNAPSHOT.war app.war

# Expose port
EXPOSE 8080

# Run it
ENTRYPOINT ["java", "-jar", "app.war"]
