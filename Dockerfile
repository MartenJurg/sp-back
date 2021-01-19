FROM openjdk:8-jdk-alpine as build

ADD . .
RUN chmod +x gradlew
RUN ./gradlew assemble


FROM openjdk:latest as production

COPY --from=build /build/libs/studentpraxis-0.0.1-SNAPSHOT.jar app.jar

VOLUME /config

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/config/application.yaml"]