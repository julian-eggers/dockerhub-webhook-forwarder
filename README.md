dockerhub-webhook-forwarder
============

[![Codacy Badge](https://api.codacy.com/project/badge/grade/dc1758219a934b4bab3b662f32354101)](https://www.codacy.com/app/eggers-julian/dockerhub-webhook-forwarder)
[![Coverage Status](https://coveralls.io/repos/julian-eggers/dockerhub-webhook-forwarder/badge.svg?branch=master&service=github)](https://coveralls.io/github/julian-eggers/dockerhub-webhook-forwarder?branch=master)
[![Build Status](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder.svg?branch=master)](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder)


## Docker
[Dockerhub](https://hub.docker.com/r/jeggers/dockerhub-webhook-forwarder/)
```
docker run \
-p 8080:8080 \
-e RABBITMQ_ADDRESSES=localhost \
-e RABBITMQ_USERNAME=guest \
-e RABBITMQ_PASSWORD=guest \
jeggers/dockerhub-webhook-forwarder:0.4.1-RELEASE
```

| Environment variable | Required | Default |
| -------------------- | -------- | ------- |
| TOKEN | no |  |
| RABBITMQ_ADDRESSES | yes | localhost |
| RABBITMQ_USERNAME | yes | guest |
| RABBITMQ_PASSWORD | yes | guest |
| RABBITMQ_EXCHANGE | no | io.docker |
| RABBITMQ_ROUTINGKEYPREFIX | no | webhookEvent (results in "webhookEvent.compressed" and "webhookEvent.original") |
| HTTP_PORT | no | 8080 |
| MAX_HEAP | no | 100M |
| JAVA_OPTS | no |  |


## Message-Content

### Compressed (Routing-Key: webhookEvent.compressed)
```json
{
  "namespace" : "jeggers",
  "repositoryName" : "dockerhub-webhook-forwarder",
  "tag" : "latest",
  "image" : "jeggers/dockerhub-webhook-forwarder:latest"
}
```

### Original (Routing-Key: webhookEvent.original)
[Dockerhub: Webhook-Example](https://docs.docker.com/docker-hub/webhooks/)