package com.itelg.docker.dwf.strategy.forwarder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

import com.itelg.docker.dwf.domain.WebHookEvent;

public class AwsSqsWebHookEventForwarder implements WebHookEventForwarder
{
    @Value("#{'${webhookevent.forward.awssqs.queues.original}'.split(',')}")
    private List<String> originalQueues;

    @Value("#{'${webhookevent.forward.awssqs.queues.compressed}'.split(',')}")
    private List<String> compressedQueues;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Override
    public void publish(WebHookEvent webHookEvent)
    {
        for (String queue : originalQueues)
        {
            if (StringUtils.hasText(queue))
            {
                queueMessagingTemplate.send(queue, MessageBuilder.withPayload(webHookEvent.getOriginalJson()).build());
            }
        }

        for (String queue : compressedQueues)
        {
            if (StringUtils.hasText(queue))
            {
                queueMessagingTemplate.send(queue, MessageBuilder.withPayload(webHookEvent.removeOriginalJson()).build());
            }
        }
    }
}
