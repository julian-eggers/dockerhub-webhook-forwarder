package com.itelg.docker.dwf.service.impl;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.service.WebhookEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultWebhookEventService implements WebhookEventService
{
    @Autowired
    private RabbitTemplate webhookEventTemplate;

    @Autowired
    private RabbitTemplate webhookEventOriginalTemplate;

    @Override
    public void publishEvent(WebhookEvent event)
    {
        if (event.getOriginalJson() != null)
        {
            webhookEventOriginalTemplate.send(MessageBuilder.withBody(event.getOriginalJson().getBytes()).setContentType("application/json").build());
        }

        event.setOriginalJson(null);
        webhookEventTemplate.convertAndSend(event);
        log.info("WebhookEvent published (" + event + ")");
    }
}