FROM openjdk:18-jdk-alpine
LABEL maintainer="he305@mail.ru"
VOLUME /tmp
EXPOSE 8080

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
