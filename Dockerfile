FROM gradle:8.13-jdk21 AS build
WORKDIR /spotify
COPY . /spotify
RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /spotify
COPY --from=build /spotify/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]