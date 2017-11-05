package com.itelg.docker.dwf.rest;

import java.nio.charset.Charset;

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

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.rest.domain.AccessDeniedException;
import com.itelg.docker.dwf.service.WebHookEventService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class WebhookEventRestControllerTest
{
    private WebhookEventRestController webhookEventRestController;

    @MockStrict
    private WebhookEventParser webhookEventParser;

    @MockStrict
    private WebHookEventService webhookEventService;

    @Before
    public void before()
    {
        webhookEventRestController = new WebhookEventRestController();
        Whitebox.setInternalState(webhookEventRestController, webhookEventService);
        Whitebox.setInternalState(webhookEventRestController, webhookEventParser);
        Whitebox.setInternalState(webhookEventRestController, "token", "");
    }

    @Test
    public void testReceive() throws Exception
    {
        webhookEventParser.parse(EasyMock.anyString());
        PowerMock.expectLastCall().andAnswer(() ->
        {
            WebHookEvent event = new WebHookEvent();
            event.setNamespace("jeggers");
            event.setRepositoryName("dockerhub-webhook-forwarder");
            event.setTag("latest");
            event.setImage("jeggers/dockerhub-webhook-forwarder:latest");
            return event;
        });

        webhookEventService.publishEvent(EasyMock.anyObject(WebHookEvent.class));
        PowerMock.expectLastCall();

        PowerMock.replayAll();
        String json = IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream(), Charset.forName("UTF-8"));
        WebHookEvent event = webhookEventRestController.receive(null, json);
        PowerMock.verifyAll();

        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        Assert.assertEquals("latest", event.getTag());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
    }

    @Test
    public void testForce() throws Exception
    {
        webhookEventService.publishEvent(EasyMock.anyObject(WebHookEvent.class));
        PowerMock.expectLastCall();

        PowerMock.replayAll();
        WebHookEvent event = webhookEventRestController.force(null, "jeggers", "dockerhub-webhook-forwarder", "latest");
        PowerMock.verifyAll();

        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        Assert.assertEquals("latest", event.getTag());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
    }

    @Test
    public void testValidateTokenValid()
    {
        Whitebox.setInternalState(webhookEventRestController, "token", "123");
        webhookEventRestController.valideToken("123");
    }

    @Test
    public void testValidateTokenNotConfigured()
    {
        Whitebox.setInternalState(webhookEventRestController, "token", "");
        webhookEventRestController.valideToken("123");
    }

    @Test(expected = AccessDeniedException.class)
    public void testValidateTokenNotValid()
    {
        Whitebox.setInternalState(webhookEventRestController, "token", "321");
        webhookEventRestController.valideToken("123");
    }
}