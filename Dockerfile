FROM amazoncorretto:17.0.7-alpine AS builder

#WORKDIR /usr/app
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=local

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM amazoncorretto:17.0.7-alpine
COPY --from=builder build/libs/*.jar app.jar

ENTRYPOINT java -jar app.jar

#EXPOSE 8080