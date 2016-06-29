package com.itelg.docker.dwf.rest;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.service.WebhookEventService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class WebhookEventRestControllerTest
{
    private WebhookEventRestController webhookEventRestController;

    @MockStrict
    private WebhookEventParser webhookEventParser;

    @MockStrict
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
            event.setRepositoryName("dockerhub-webhook-forwarder");
            event.setTag("latest");
            event.setImage("jeggers/dockerhub-webhook-forwarder:latest");
            return event;
        });

        webhookEventService.publishEvent(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall();

        PowerMock.replayAll();
        String json = IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream());
        WebhookEvent event = webhookEventRestController.receive(json);
        PowerMock.verifyAll();

        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        Assert.assertEquals("latest", event.getTag());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
    }

    @Test
    public void testForce() throws Exception
    {
        webhookEventService.publishEvent(EasyMock.anyObject(WebhookEvent.class));
        PowerMock.expectLastCall();

        PowerMock.replayAll();
        WebhookEvent event = webhookEventRestController.force("jeggers", "dockerhub-webhook-forwarder", "latest");
        PowerMock.verifyAll();

        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        Assert.assertEquals("latest", event.getTag());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
    }
}