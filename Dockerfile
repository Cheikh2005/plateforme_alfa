FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY backend/target/bacprep-backend-1.0.0.jar app.jar
EXPOSE 10000
CMD ["sh", "-c", "java -Xmx350m -XX:MaxMetaspaceSize=128m -Dspring.profiles.active=prod -Dserver.port=${PORT:-10000} -jar app.jar"]