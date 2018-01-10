package com.itelg.docker.dwf.strategy.forwarder;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.itelg.docker.dwf.domain.WebHookEvent;

public class RabbitMqWebHookEventForwarder implements WebHookEventForwarder
{
    @Autowired
    private RabbitTemplate webHookEventOriginalTemplate;

    @Autowired
    private RabbitTemplate webHookEventCompressedTemplate;

    @Override
    public void publish(WebHookEvent webHookEvent)
    {
        if (StringUtils.hasText(webHookEvent.getOriginalJson()))
        {
            webHookEventOriginalTemplate.send(MessageBuilder.withBody(webHookEvent.getOriginalJson().getBytes()).setContentType("application/json").build());
        }

        webHookEventCompressedTemplate.convertAndSend(webHookEvent.removeOriginalJson());
    }
}
