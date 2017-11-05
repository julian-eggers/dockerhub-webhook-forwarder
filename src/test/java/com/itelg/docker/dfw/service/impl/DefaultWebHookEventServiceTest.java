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

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.service.WebHookEventService;
import com.itelg.docker.dwf.service.impl.DefaultWebHookEventService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class DefaultWebHookEventServiceTest
{
    private WebHookEventService webHookEventService;

    @MockStrict
    private RabbitTemplate webHookEventTemplate;

    @Mock
    private RabbitTemplate webHookEventOriginalTemplate;

    @Before
    public void before()
    {
        webHookEventService = new DefaultWebHookEventService();
        Whitebox.setInternalState(webHookEventService, "webHookEventTemplate", webHookEventTemplate);
        Whitebox.setInternalState(webHookEventService, "webHookEventOriginalTemplate", webHookEventOriginalTemplate);
    }

    @Test
    public void testPublishEvent()
    {
        webHookEventTemplate.convertAndSend(EasyMock.anyObject(WebHookEvent.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            WebHookEvent webhookEvent = (WebHookEvent) EasyMock.getCurrentArguments()[0];
            Assert.assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        PowerMock.replayAll();
        WebHookEvent webhookEvent = new WebHookEvent();
        webHookEventService.publishEvent(webhookEvent);
        PowerMock.verifyAll();
    }

    @Test
    public void testPublishOriginalEvent()
    {
        webHookEventOriginalTemplate.send(EasyMock.anyObject(Message.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            Message message = (Message) EasyMock.getCurrentArguments()[0];
            Assert.assertEquals("Testtest", new String(message.getBody()));
            Assert.assertEquals("application/json", message.getMessageProperties().getContentType());
            return null;
        });

        webHookEventTemplate.convertAndSend(EasyMock.anyObject(WebHookEvent.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            WebHookEvent webhookEvent = (WebHookEvent) EasyMock.getCurrentArguments()[0];
            Assert.assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        PowerMock.replayAll();
        WebHookEvent webhookEvent = new WebHookEvent();
        webhookEvent.setOriginalJson("Testtest");
        webHookEventService.publishEvent(webhookEvent);
        PowerMock.verifyAll();
    }
}