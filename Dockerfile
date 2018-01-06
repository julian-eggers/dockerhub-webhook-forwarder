FROM openjdk:9-jre-slim
EXPOSE 8080
ADD target/dockerhub-webhook-forwarder.jar dockerhub-webhook-forwarder.jar
ENTRYPOINT ["java", "-jar", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "dockerhub-webhook-forwarder.jar"]
