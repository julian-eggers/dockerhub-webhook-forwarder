package com.itelg.docker.dwf.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.itelg.docker.dwf.DomainTestSupport;
import com.itelg.docker.dwf.domain.WebHookEvent;

public class WebhookEventParserTest implements DomainTestSupport
{
    @Test
    public void testParse()
    {
        WebHookEvent event = new WebhookEventParser().parse(getOriginalWebHookEventJson());
        assertEquals("jeggers", event.getNamespace());
        assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        assertEquals("latest", event.getTag());
        assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
        assertEquals(getOriginalWebHookEventJson(), event.getOriginalJson());
    }
}