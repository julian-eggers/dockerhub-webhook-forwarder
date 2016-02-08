package com.itelg.docker.dfw.service.impl;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.service.WebhookEventService;
import com.itelg.docker.dwf.service.impl.DefaultWebhookEventService;

@RunWith(PowerMockRunner.class)
public class DefaultWebhookEventServiceTest
{
    private WebhookEventService webhookEventService;

    @Mock
    private RabbitTemplate eventPublishTemplate;
    
    @Before
    public void before()
    {
        webhookEventService = new DefaultWebhookEventService();
        Whitebox.setInternalState(webhookEventService, eventPublishTemplate);
    }
    
    @Test
    public void testPublishEvent()
    {
        eventPublishTemplate.convertAndSend(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        webhookEventService.publishEvent(new WebhookEvent());
        PowerMock.verifyAll();
    }
}