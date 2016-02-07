package com.itelg.docker.dwf.service;

import com.itelg.docker.dwf.domain.WebhookEvent;

public interface WebhookEventService
{
    public void publishEvent(WebhookEvent event);
}