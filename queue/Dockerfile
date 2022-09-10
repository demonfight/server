FROM eclipse-temurin:18

COPY build/libs/ServerQueue.jar /server
WORKDIR /server

RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive \
  apt-get install -y \
    sudo \
    curl \
    unzip \
    nano \
    && apt-get clean

EXPOSE 25565

ENTRYPOINT ["java", "-jar", "ServerQueue.jar"]
