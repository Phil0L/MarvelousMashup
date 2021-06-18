FROM openjdk:11

COPY ./ /opt/server
RUN chmod +x /opt/server/gradlew

WORKDIR /opt/server/

ENTRYPOINT /opt/server/gradlew run