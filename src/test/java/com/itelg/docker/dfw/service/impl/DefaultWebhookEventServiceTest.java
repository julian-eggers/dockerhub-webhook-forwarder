package com.itelg.docker.dfw.service.impl;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.service.WebhookEventService;
import com.itelg.docker.dwf.service.impl.DefaultWebhookEventService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class DefaultWebhookEventServiceTest
{
    private WebhookEventService webhookEventService;

    @MockStrict
    private RabbitTemplate webhookEventTemplate;

    @Mock
    private RabbitTemplate webhookEventOriginalTemplate;

    @Before
    public void before()
    {
        System.out.println("test");
        System.out.println(webhookEventTemplate);
        System.out.println(webhookEventOriginalTemplate);
        webhookEventService = new DefaultWebhookEventService();
        Whitebox.setInternalState(webhookEventService, "webhookEventTemplate", webhookEventTemplate);
        Whitebox.setInternalState(webhookEventService, "webhookEventOriginalTemplate", webhookEventOriginalTemplate);
    }

    @Test
    public void testPublishEvent()
    {
        webhookEventTemplate.convertAndSend(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            WebhookEvent webhookEvent = (WebhookEvent) EasyMock.getCurrentArguments()[0];
            Assert.assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        PowerMock.replayAll();
        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEventService.publishEvent(webhookEvent);
        PowerMock.verifyAll();
    }

    @Test
    public void testPublishOriginalEvent()
    {
        webhookEventOriginalTemplate.send(EasyMock.anyObject(Message.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            Message message = (Message) EasyMock.getCurrentArguments()[0];
            Assert.assertEquals("Testtest", new String(message.getBody()));
            Assert.assertEquals("application/json", message.getMessageProperties().getContentType());
            return null;
        });

        webhookEventTemplate.convertAndSend(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            WebhookEvent webhookEvent = (WebhookEvent) EasyMock.getCurrentArguments()[0];
            Assert.assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        PowerMock.replayAll();
        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setOriginalJson("Testtest");
        webhookEventService.publishEvent(webhookEvent);
        PowerMock.verifyAll();
    }
}