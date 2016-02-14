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
-e rabbitmq_addresses=localhost \
-e rabbitmq_username=guest \
-e rabbitmq_password=guest \
jeggers/dockerhub-webhook-forwarder
```

| Environment variable  | Required | Default |
| ------------- | ------------- | ------------- |
| rabbitmq_addresses  | yes  | localhost |
| rabbitmq_username  | yes  | guest |
| rabbitmq_password  | yes  | guest |
| rabbitmq_exchange  | no  | io.docker |
| rabbitmq_routingkey  | no  | webhookEvent |


## Message-Content
```xml
<webhookEvent namespace="jeggers" repository="jeggers/dockerhub-webhook-forwarder" tag="latest" />
```
