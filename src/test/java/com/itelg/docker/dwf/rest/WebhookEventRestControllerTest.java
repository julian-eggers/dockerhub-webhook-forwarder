package com.itelg.docker.dwf.rest;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.service.WebhookEventService;

@RunWith(PowerMockRunner.class)
public class WebhookEventRestControllerTest
{
    private WebhookEventRestController webhookEventRestController;
    
    @Mock
    private WebhookEventParser webhookEventParser;
    
    @Mock
    private WebhookEventService webhookEventService;
    
    @Before
    public void before()
    {
        webhookEventRestController = new WebhookEventRestController();
        Whitebox.setInternalState(webhookEventRestController, webhookEventService);
        Whitebox.setInternalState(webhookEventRestController, webhookEventParser);
    }

    @Test
    public void testReceive() throws Exception
    {
        webhookEventParser.parse(EasyMock.anyString());
        PowerMock.expectLastCall().andAnswer(() -> 
        {
            WebhookEvent event = new WebhookEvent();
            event.setNamespace("jeggers");
            event.setRepository("jeggers/dockerhub-webhook-forwarder");
            event.setTag("latest");
            return event;
        });
        
        webhookEventService.publishEvent(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        String json = IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream());
        WebhookEvent event = webhookEventRestController.receive(json);
        PowerMock.verifyAll();

        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder", event.getRepository());
        Assert.assertEquals("latest", event.getTag());
    }
    
    @Test
    public void testForce() throws Exception
    {
        webhookEventService.publishEvent(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        WebhookEvent event = webhookEventRestController.force("jeggers", "jeggers/dockerhub-webhook-forwarder", "latest");
        PowerMock.verifyAll();
        
        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder", event.getRepository());
        Assert.assertEquals("latest", event.getTag());
    }
}