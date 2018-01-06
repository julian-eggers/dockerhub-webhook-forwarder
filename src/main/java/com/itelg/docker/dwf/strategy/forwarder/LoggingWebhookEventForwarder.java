package com.itelg.docker.dwf.strategy.forwarder;

import org.springframework.stereotype.Component;

import com.itelg.docker.dwf.domain.WebHookEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingWebhookEventForwarder implements WebhookEventForwarder
{
    @Override
    public void publish(WebHookEvent webHookEvent)
    {
        log.info("Publishing " + webHookEvent);
    }
}
