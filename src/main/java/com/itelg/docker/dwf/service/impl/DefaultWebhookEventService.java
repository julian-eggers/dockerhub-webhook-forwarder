package com.itelg.docker.dwf.service.impl;

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
    private RabbitTemplate eventPublishTemplate;

    @Override
    public void publishEvent(WebhookEvent event)
    {
        eventPublishTemplate.convertAndSend(event);
        log.info("WebhookEvent published (" + event + ")");
    }
}