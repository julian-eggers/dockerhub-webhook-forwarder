dockerhub-webhook-forwarder
===========================

[![Codacy Badge](https://api.codacy.com/project/badge/grade/dc1758219a934b4bab3b662f32354101)](https://www.codacy.com/app/eggers-julian/dockerhub-webhook-forwarder)
[![Coverage Status](https://coveralls.io/repos/julian-eggers/dockerhub-webhook-forwarder/badge.svg?branch=master&service=github)](https://coveralls.io/github/julian-eggers/dockerhub-webhook-forwarder?branch=master)
[![Build Status](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder.svg?branch=master)](https://travis-ci.org/julian-eggers/dockerhub-webhook-forwarder)

Provides a dockerhub webhook-endpoint and forwards events to a message broker like RabbitMQ or AWS SQS.

## Docker
[Dockerhub](https://hub.docker.com/r/jeggers/dockerhub-webhook-forwarder/)

## Setup
1. Start dockerhub-webhook-forwarder (Docker recommended, [Examples](https://github.com/julian-eggers/dockerhub-webhook-forwarder/wiki#docker-examples))
2. 


| Property | Required | Default |
| -------- | -------- | ------- |
| --request.token | no |  |


## Event forwarding
You can choose between the [original event](https://docs.docker.com/docker-hub/webhooks/) or the following compressed version.
```json
{
  "namespace" : "jeggers",
  "repositoryName" : "dockerhub-webhook-forwarder",
  "tag" : "latest",
  "image" : "jeggers/dockerhub-webhook-forwarder:latest"
}
```

### RabbitMQ

| Property | Required | Default |
| -------- | -------- | ------- |
| --webhookevent.forward.rabbitmq.hosts | yes |  |
| --webhookevent.forward.rabbitmq.username | no | guest |
| --webhookevent.forward.rabbitmq.password | no | guest |
| --webhookevent.forward.rabbitmq.exchange.name | no | io.docker |
| --webhookevent.forward.rabbitmq.routing-key.prefix | no | webHookEvent (results in "webHookEvent.compressed" and "webHookEvent.original") |


### AWS SQS

| Property | Required | Default | Info |
| -------- | -------- | ------- | ---- |
| --webhookevent.forward.awssqs.access-key | yes |  | [Policy](https://github.com/julian-eggers/dockerhub-webhook-forwarder/wiki/AWS-SQS#policy) |
| --webhookevent.forward.awssqs.secret-key | yes |  |  |
| --webhookevent.forward.awssqs.region | yes |  | [Regions](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/regions/Regions.html) |
| --webhookevent.forward.awssqs.queues.original | no |  |  |
| --webhookevent.forward.awssqs.queues.compressed | no |  |  |

You have to specify at least one queue.
