FROM maven:3.8.1-jdk-11 as build
WORKDIR /app
COPY . /app
RUN mvn clean package

# Use OpenJDK for running the application
FROM openjdk:11-jre-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
