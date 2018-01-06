package com.itelg.docker.dwf.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Arrays;

import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class DefaultWebHookEventServiceTest
{
    @TestSubject
    private WebHookEventService webHookEventService = new DefaultWebHookEventService();

    @Mock
    private WebHookEventForwarder webHookEventForwarder1;

    @Mock
    private WebHookEventForwarder webHookEventForwarder2;

    @Before
    public void before()
    {
        setInternalState(webHookEventService, Arrays.asList(webHookEventForwarder1, webHookEventForwarder2));
    }

    @Test
    public void testPublishEvent()
    {
        webHookEventForwarder1.publish(anyObject(WebHookEvent.class));
        webHookEventForwarder2.publish(anyObject(WebHookEvent.class));

        replayAll();
        WebHookEvent webhookEvent = new WebHookEvent();
        webHookEventService.publishEvent(webhookEvent);
        verifyAll();
    }
}