package com.itelg.docker.dwf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

@Service
public class DefaultWebHookEventService implements WebHookEventService
{
    @Autowired
    private List<WebHookEventForwarder> webhookEventForwarders;

    @Override
    public void publishEvent(WebHookEvent webHookEvent)
    {
        for (WebHookEventForwarder forwarder : webhookEventForwarders)
        {
            forwarder.publish(webHookEvent);
        }
    }
}