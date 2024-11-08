FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY apihello/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]