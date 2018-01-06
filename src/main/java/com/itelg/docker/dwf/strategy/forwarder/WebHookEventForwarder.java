package com.itelg.docker.dwf.strategy.forwarder;

import com.itelg.docker.dwf.domain.WebHookEvent;

public interface WebHookEventForwarder
{
    void publish(WebHookEvent webHookEvent);
}
