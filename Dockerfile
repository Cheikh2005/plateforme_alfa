# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
ENV MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m"
COPY backend/pom.xml .
RUN mvn dependency:go-offline -q
COPY backend/src ./src
RUN mvn clean package -DskipTests -q

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/bacprep-backend-1.0.0.jar app.jar
EXPOSE 10000
CMD ["sh", "-c", "java -Xmx350m -XX:MaxMetaspaceSize=128m -Dspring.profiles.active=prod -Dserver.port=${PORT:-10000} -jar app.jar"]