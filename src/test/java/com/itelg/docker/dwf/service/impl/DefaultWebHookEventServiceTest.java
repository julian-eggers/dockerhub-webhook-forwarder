package com.itelg.docker.dwf.service.impl;

import static org.easymock.EasyMock.anyDouble;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Arrays;

import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

import com.itelg.docker.dwf.DomainTestSupport;
import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

@RunWith(PowerMockRunner.class)
public class DefaultWebHookEventServiceTest implements DomainTestSupport
{
    @TestSubject
    private WebHookEventService webHookEventService = new DefaultWebHookEventService();

    @Mock
    private WebHookEventForwarder webHookEventForwarder1;

    @Mock
    private WebHookEventForwarder webHookEventForwarder2;

    @MockStrict
    private CounterService counterService;

    @Mock
    private GaugeService gaugeService;

    @Before
    public void before()
    {
        setInternalState(webHookEventService, Arrays.asList(webHookEventForwarder1, webHookEventForwarder2));
        setInternalState(webHookEventService, gaugeService);
    }

    @Test
    public void testPublishEvent()
    {
        counterService.increment("event_inbound_total");
        gaugeService.submit(eq("event_inbound_last_timestamp"), anyDouble());

        webHookEventForwarder1.publish(anyObject(WebHookEvent.class));
        counterService.increment("event_forwarded_total");

        webHookEventForwarder2.publish(anyObject(WebHookEvent.class));
        counterService.increment("event_forwarded_total");

        replayAll();
        webHookEventService.publishEvent(getCompleteWebHookEvent());
        verifyAll();
    }
}