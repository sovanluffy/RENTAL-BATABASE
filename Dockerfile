FROM eclipse-temurin:17-jdk AS build

RUN apt-get update && apt-get install -y \
    maven \
    git \
    bash \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]