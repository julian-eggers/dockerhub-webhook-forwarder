package com.itelg.docker.dwf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.metrics.Metrics;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

import io.micrometer.core.instrument.MeterRegistry;

@Service
public class DefaultWebHookEventService implements WebHookEventService
{
    @Autowired
    private List<WebHookEventForwarder> webhookEventForwarders;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void publishEvent(WebHookEvent webHookEvent)
    {
        meterRegistry.counter(Metrics.EVENT_INBOUND_TOTAL_COUNT).increment();
        meterRegistry.gauge(Metrics.EVENT_INBOUND_LAST_TIMESTAMP, System.currentTimeMillis());

        for (WebHookEventForwarder forwarder : webhookEventForwarders)
        {
            forwarder.publish(webHookEvent);
            meterRegistry.counter(Metrics.FORWARDEDTO_TOTAL_COUNT).increment();
        }
    }
}