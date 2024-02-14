FROM openjdk:8-jdk-slim

RUN mkdir /app
WORKDIR /app
COPY . .

RUN ./gradlew quoridor-gui:fatJar
