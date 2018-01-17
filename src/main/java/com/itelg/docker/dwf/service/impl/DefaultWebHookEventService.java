package com.itelg.docker.dwf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.metrics.Metrics;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

@Service
public class DefaultWebHookEventService implements WebHookEventService
{
    @Autowired
    private List<WebHookEventForwarder> webhookEventForwarders;

    @Autowired
    private CounterService counterService;

    @Override
    public void publishEvent(WebHookEvent webHookEvent)
    {
        counterService.increment(Metrics.EVENT_INBOUND_TOTAL);

        for (WebHookEventForwarder forwarder : webhookEventForwarders)
        {
            forwarder.publish(webHookEvent);
            counterService.increment(Metrics.FORWARDED_TO_TOTAL);
        }
    }
}