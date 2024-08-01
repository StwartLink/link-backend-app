FROM openjdk:21-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} visita-service.jar
ENTRYPOINT ["java", "-jar", "/visita-service.jar"]