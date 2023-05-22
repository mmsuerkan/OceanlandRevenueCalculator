FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app/app.jar"]