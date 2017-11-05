package com.itelg.docker.dwf.service.impl;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultWebHookEventService implements WebHookEventService
{
    @Autowired
    private RabbitTemplate webHookEventTemplate;

    @Autowired
    private RabbitTemplate webHookEventOriginalTemplate;

    @Override
    public void publishEvent(WebHookEvent event)
    {
        log.info("Publishing " + event);

        if (event.getOriginalJson() != null)
        {
            webHookEventOriginalTemplate.send(MessageBuilder.withBody(event.getOriginalJson().getBytes()).setContentType("application/json").build());
        }

        event.setOriginalJson(null);
        webHookEventTemplate.convertAndSend(event);
    }
}