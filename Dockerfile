FROM openjdk:21-jdk

WORKDIR /app

COPY target/hyperativa-0.0.1-SNAPSHOT.jar /app/hyperativa-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/hyperativa-0.0.1-SNAPSHOT.jar"]