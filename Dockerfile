FROM java:8

EXPOSE 8081

COPY PigeonIMWeb-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]