FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR file (NOT WAR)
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
