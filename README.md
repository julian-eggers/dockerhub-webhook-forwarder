dockerhub-webhook-forwarder
===========================

[![Codacy Badge](https://api.codacy.com/project/badge/grade/dc1758219a934b4bab3b662f32354101)](https://www.codacy.com/app/eggers-julian/dockerhub-webhook-forwarder)
[![Coverage Status](https://coveralls.io/repos/julian-eggers/dockerhub-webhook-forwarder/badge.svg?branch=master&service=github)](https://coveralls.io/github/julian-eggers/dockerhub-webhook-forwarder?branch=master)
[![Build Status](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder.svg?branch=master)](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder)


## Docker
[Dockerhub](https://hub.docker.com/r/jeggers/dockerhub-webhook-forwarder/)
```
docker run \
-d \
--name=dockerhub-webhook-forwarder \
--restart=always \
-p 8080:8080 \
jeggers/dockerhub-webhook-forwarder:latest \
--webhookevent.forward.rabbitmq.hosts=localhost \
--webhookevent.forward.rabbitmq.username=guest \
--webhookevent.forward.rabbitmq.password=guest
```

| Property | Required | Default |
| -------- | -------- | ------- |
| --request.token | no |  |
| --webhookevent.forward.rabbitmq.hosts | no |  |
| --webhookevent.forward.rabbitmq.username | no | guest |
| --webhookevent.forward.rabbitmq.password | no | guest |
| --webhookevent.forward.rabbitmq.exchange.name | no | io.docker |
| --webhookevent.forward.rabbitmq.routing-key.prefix | no | webHookEvent (results in "webHookEvent.compressed" and "webHookEvent.original") |


## Message-Content

### Compressed (Routing-Key: webHookEvent.compressed)
```json
{
  "namespace" : "jeggers",
  "repositoryName" : "dockerhub-webhook-forwarder",
  "tag" : "latest",
  "image" : "jeggers/dockerhub-webhook-forwarder:latest"
}
```

### Original (Routing-Key: webHookEvent.original)
[Dockerhub: Webhook-Example](https://docs.docker.com/docker-hub/webhooks/)
