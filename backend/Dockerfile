FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/registration-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8088
CMD ["java", "-jar", "app.jar"]
