package com.itelg.docker.dwf.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.service.WebhookEventService;

@Service
public class DefaultWebhookEventService implements WebhookEventService
{
    private static final Logger log = LoggerFactory.getLogger(DefaultWebhookEventService.class);

    @Autowired
    private RabbitTemplate eventPublishTemplate;
    
    @Override
    public void publishEvent(WebhookEvent event)
    {
        eventPublishTemplate.convertAndSend(event);
        log.info("WebhookEvent published (" + event + ")");
    }
}