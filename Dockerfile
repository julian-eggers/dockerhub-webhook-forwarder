FROM openjdk:9-jre-alpine

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dockerhub-webhook-forwarder.jar"]

ADD target/dockerhub-webhook-forwarder.jar dockerhub-webhook-forwarder.jar