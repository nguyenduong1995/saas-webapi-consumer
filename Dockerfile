FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar
ARG DEPENDENCY=target/libs/*.jar

WORKDIR /app
WORKDIR /app/libs
WORKDIR /app/conf
WORKDIR /app/logs

WORKDIR /app

COPY ${JAR_FILE} /app/
COPY ${DEPENDENCY} /app/libs/

CMD ["java","-cp","saas-consumer-api.jar:libs/*.jar","-Dfile.encoding=UTF-8", "-Xms1g", "-Xmx4g", "-Dspring.config.location=file:///app/conf/", "co.ipicorp.saas.consumerapi.ConsumerApiApplication"]