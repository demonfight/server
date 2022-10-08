FROM eclipse-temurin:18

COPY build/libs/ServerQueue.jar /server
WORKDIR /server

EXPOSE 25565

ENTRYPOINT ["java", "-jar", "ServerQueue.jar"]
