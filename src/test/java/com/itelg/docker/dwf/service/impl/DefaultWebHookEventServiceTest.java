package com.itelg.docker.dwf.service.impl;

import static org.easymock.EasyMock.anyDouble;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.mockito.Mockito.mock;
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

import com.itelg.docker.dwf.DomainTestSupport;
import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

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
    private MeterRegistry meterRegistry;

    @Before
    public void before()
    {
        setInternalState(webHookEventService, Arrays.asList(webHookEventForwarder1, webHookEventForwarder2));
    }

    @Test
    public void testPublishEvent()
    {
        meterRegistry.counter("event_inbound_total_sum");
        expectLastCall().andReturn(mock(Counter.class));

        meterRegistry.gauge(eq("event_inbound_last_timestamp"), anyDouble());
        expectLastCall().andReturn(null);

        webHookEventForwarder1.publish(anyObject(WebHookEvent.class));

        meterRegistry.counter("event_forwardedto_total_sum");
        expectLastCall().andReturn(mock(Counter.class));

        webHookEventForwarder2.publish(anyObject(WebHookEvent.class));

        meterRegistry.counter("event_forwardedto_total_sum");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventService.publishEvent(getCompleteWebHookEvent());
        verifyAll();
    }
}