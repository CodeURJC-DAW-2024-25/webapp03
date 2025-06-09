# Stage frontend
FROM node:20.19-alpine AS frontend
WORKDIR /project
COPY /frontend .
RUN npm install
RUN npm run build --configuration production

# Stage backend
FROM maven:3.9.6-sapmachine-21 AS backend
WORKDIR /project
COPY backend/pom.xml /project/
RUN mvn clean install
COPY backend/src /project/src
COPY --from=frontend /project/libace/dist/libace/browser /project/src/main/resources/static/new
RUN mvn clean package -DskipTests

# Execution
FROM openjdk:21-jdk-slim
WORKDIR /usr/src/app/target
COPY --from=backend /project/target/*.jar /usr/src/app/target/app.jar
EXPOSE 8443
CMD ["java", "-jar", "app.jar"]
