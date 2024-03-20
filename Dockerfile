FROM openjdk:17-alpine

WORKDIR /app

COPY devridge-api/build/libs/devridge-*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/config/", "app.jar"]
