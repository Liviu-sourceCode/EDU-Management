
FROM openjdk:17-alpine
WORKDIR /app
COPY target/springdata-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "/app.jar"]
