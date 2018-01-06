package com.itelg.docker.dwf.service;

import com.itelg.docker.dwf.domain.WebHookEvent;

public interface WebHookEventService
{
    public void publishEvent(WebHookEvent webHookEvent);
}