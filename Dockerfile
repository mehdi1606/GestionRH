# Build stage
FROM maven:3.8.2-openjdk-17 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -Pprod -DskipTests

# Package stage
FROM openjdk:17-slim
COPY --from=build /usr/src/app/target/GestionRH-0.0.1-SNAPSHOT.jar /GestionRH.jar
# ENV PORT=8090
EXPOSE 8090
ENTRYPOINT ["java","-jar","/GestionRH.jar"]
