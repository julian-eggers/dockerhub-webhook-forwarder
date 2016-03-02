FROM jeanblanchard/java:serverjre-8

VOLUME /var/log/apps

ENV rabbitmq_addresses		localhost
ENV rabbitmq_username		guest
ENV rabbitmq_password		guest
ENV rabbitmq_exchange		io.docker
ENV rabbitmq_routingkey		webhookEvent
ENV JAVA_OPTS				""

COPY dockerhub-webhook-forwarder.jar dockerhub-webhook-forwarder.jar

ENTRYPOINT ["java", \ 
"-Dspring.rabbitmq.addresses=${rabbitmq_addresses}", \
"-Dspring.rabbitmq.username=${rabbitmq_username}", \
"-Dspring.rabbitmq.password=${rabbitmq_password}", \
"-Dspring.rabbitmq.exchange=${rabbitmq_exchange}", \
"-Dspring.rabbitmq.routingkey=${rabbitmq_routingkey} ${JAVA_OPTS}", \
"-jar", \
"/dockerhub-webhook-forwarder.jar"]