FROM openjdk:21
MAINTAINER com.therealazimbek
COPY target/currency-bot-0.0.1-SNAPSHOT.jar currency-bot-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "currency-bot-0.0.1-SNAPSHOT.jar"]