package com.itelg.docker.dwf.rest;

import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import org.easymock.EasyMock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;

import com.itelg.docker.dwf.DomainTestSupport;
import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.rest.domain.AccessDeniedException;
import com.itelg.docker.dwf.service.WebHookEventService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RunWith(PowerMockRunner.class)
public class WebhookEventRestControllerTest implements DomainTestSupport
{
    @TestSubject
    private WebhookEventRestController webhookEventRestController = new WebhookEventRestController();

    @MockStrict
    private WebhookEventParser webhookEventParser;

    @MockStrict
    private WebHookEventService webhookEventService;

    @MockStrict
    private MeterRegistry meterRegistry;

    @Before
    public void before()
    {
        setInternalState(webhookEventRestController, "token", "");
    }

    @Test
    public void testReceive() throws Exception
    {
        webhookEventParser.parse(EasyMock.anyString());
        expectLastCall().andReturn(getCompleteWebHookEvent());

        webhookEventService.publishEvent(EasyMock.anyObject(WebHookEvent.class));

        meterRegistry.counter("event_inbound_bysource_count", "source", "WebHookEvent");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        WebHookEvent event = webhookEventRestController.receive(null, getOriginalWebHookEventJson());
        verifyAll();

        assertEquals("jeggers", event.getNamespace());
        assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        assertEquals("latest", event.getTag());
        assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
        assertEquals(getOriginalWebHookEventJson(), event.getOriginalJson());
    }

    @Test
    public void testForce() throws Exception
    {
        webhookEventService.publishEvent(EasyMock.anyObject(WebHookEvent.class));

        meterRegistry.counter("event_inbound_bysource_count", "source", "force");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        WebHookEvent event = webhookEventRestController.force(null, "jeggers", "dockerhub-webhook-forwarder", "latest");
        verifyAll();

        assertEquals("jeggers", event.getNamespace());
        assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        assertEquals("latest", event.getTag());
        assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
    }

    @Test
    public void testValidateWithoutConfiguredToken()
    {
        setInternalState(webhookEventRestController, "token", "");

        replayAll();
        webhookEventRestController.valideToken("123");
        verifyAll();
    }

    @Test
    public void testValidateWithValidToken()
    {
        setInternalState(webhookEventRestController, "token", "123");

        replayAll();
        webhookEventRestController.valideToken("123");
        verifyAll();
    }

    @Test(expected = AccessDeniedException.class)
    public void testValidateTokenNotValid()
    {
        setInternalState(webhookEventRestController, "token", "321");

        meterRegistry.counter("rest_authentication_failed_total_count");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webhookEventRestController.valideToken("123");
        verifyAll();
    }
}