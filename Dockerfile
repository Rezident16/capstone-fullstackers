# Stage 1: Build the React frontend
FROM node:14 AS frontend-build

# Set the working directory
WORKDIR /client

# Copy package.json and package-lock.json
COPY client/package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the frontend code
COPY client/ .

# Build the React application
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.8.1-openjdk-11 AS backend-build

# Set the working directory
WORKDIR /server/stock_connect

# Copy the pom.xml file and install dependencies
COPY server/stock_connect/pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the backend code
COPY server/stock_connect/src ./src

# Package the application, skipping tests
RUN mvn package -DskipTests

# Stage 3: Run the application
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the packaged backend application
COPY --from=backend-build /server/stock_connect/target/*.jar app.jar

# Copy the built frontend application
COPY --from=frontend-build /client/build ./public

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
