# Compilation
FROM maven:3.9.6-sapmachine-21 AS builder
WORKDIR /project
COPY backend/pom.xml /project/
COPY backend/src /project/src

RUN mvn clean package -DskipTests

# Execution
FROM openjdk:21-jdk-slim
WORKDIR /usr/src/app/target
COPY --from=builder /project/target/*.jar /usr/src/app/target/app.jar
EXPOSE 8443
CMD ["java", "-jar", "app.jar"]
