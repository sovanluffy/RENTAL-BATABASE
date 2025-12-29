# Stage 1: build
FROM eclipse-temurin:17-jdk-alpine AS build

RUN apk add --no-cache maven git bash

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jdk-alpine AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
