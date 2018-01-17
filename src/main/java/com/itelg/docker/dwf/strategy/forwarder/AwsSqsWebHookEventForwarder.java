package com.itelg.docker.dwf.strategy.forwarder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.metrics.Metrics;

import lombok.SneakyThrows;

public class AwsSqsWebHookEventForwarder implements WebHookEventForwarder
{
    @Value("#{'${webhookevent.forward.awssqs.queues.original}'.split(',')}")
    private List<String> originalQueues;

    @Value("#{'${webhookevent.forward.awssqs.queues.compressed}'.split(',')}")
    private List<String> compressedQueues;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CounterService counterService;

    @Override
    public void publish(WebHookEvent webHookEvent)
    {
        for (String queue : originalQueues)
        {
            if (StringUtils.hasText(queue) && StringUtils.hasText(webHookEvent.getOriginalJson()))
            {
                queueMessagingTemplate.send(queue, MessageBuilder.withPayload(webHookEvent.getOriginalJson()).build());
                counterService.increment(Metrics.forwardedTo("awssqs-original"));
            }
        }

        for (String queue : compressedQueues)
        {
            if (StringUtils.hasText(queue))
            {
                String json = toJson(webHookEvent.removeOriginalJson());
                queueMessagingTemplate.send(queue, MessageBuilder.withPayload(json).build());
                counterService.increment(Metrics.forwardedTo("awssqs-compressed"));
            }
        }
    }

    @SneakyThrows
    private String toJson(WebHookEvent webHookEvent)
    {
        return objectMapper.writeValueAsString(webHookEvent);
    }
}
