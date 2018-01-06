package com.itelg.docker.dwf.strategy.forwarder;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.itelg.docker.dwf.domain.WebHookEvent;

@RunWith(PowerMockRunner.class)
public class RabbitMqWebHookEventForwarderTest
{
    private WebHookEventForwarder webHookEventForwarder = new RabbitMqWebHookEventForwarder();;

    @Mock
    private RabbitTemplate webHookEventOriginalTemplate;

    @MockStrict
    private RabbitTemplate webHookEventCompressedTemplate;

    @Before
    public void before()
    {
        setInternalState(webHookEventForwarder, "webHookEventOriginalTemplate", webHookEventOriginalTemplate);
        setInternalState(webHookEventForwarder, "webHookEventCompressedTemplate", webHookEventCompressedTemplate);
    }

    @Test
    public void testPublishEventWithoutOriginalJson()
    {
        webHookEventCompressedTemplate.convertAndSend(anyObject(WebHookEvent.class));
        expectLastCall().andAnswer(() ->
        {
            WebHookEvent webhookEvent = (WebHookEvent) getCurrentArguments()[0];
            assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        replayAll();
        WebHookEvent webhookEvent = new WebHookEvent();
        webHookEventForwarder.publish(webhookEvent);
        verifyAll();
    }

    @Test
    public void testPublishOriginalEvent()
    {
        webHookEventOriginalTemplate.send(anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            Message message = (Message) getCurrentArguments()[0];
            assertEquals("Testtest", new String(message.getBody()));
            assertEquals("application/json", message.getMessageProperties().getContentType());
            return null;
        });

        webHookEventCompressedTemplate.convertAndSend(anyObject(WebHookEvent.class));
        expectLastCall().andAnswer(() ->
        {
            WebHookEvent webhookEvent = (WebHookEvent) getCurrentArguments()[0];
            assertNull(webhookEvent.getOriginalJson());
            return null;
        });

        replayAll();
        WebHookEvent webhookEvent = new WebHookEvent();
        webhookEvent.setOriginalJson("Testtest");
        webHookEventForwarder.publish(webhookEvent);
        verifyAll();
    }
}
