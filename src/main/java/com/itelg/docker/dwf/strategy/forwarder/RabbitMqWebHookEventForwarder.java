package com.itelg.docker.dwf.strategy.forwarder;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.util.StringUtils;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.metrics.Metrics;

public class RabbitMqWebHookEventForwarder implements WebHookEventForwarder
{
    @Autowired
    private RabbitTemplate webHookEventOriginalTemplate;

    @Autowired
    private RabbitTemplate webHookEventCompressedTemplate;

    @Autowired
    private CounterService counterService;

    @Override
    public void publish(WebHookEvent webHookEvent)
    {
        if (StringUtils.hasText(webHookEvent.getOriginalJson()))
        {
            webHookEventOriginalTemplate.send(MessageBuilder.withBody(webHookEvent.getOriginalJson().getBytes()).setContentType("application/json").build());
            counterService.increment(Metrics.forwardedTo("rabbitmq-original"));
        }

        webHookEventCompressedTemplate.convertAndSend(webHookEvent.removeOriginalJson());
        counterService.increment(Metrics.forwardedTo("rabbitmq-compressed"));
    }
}
