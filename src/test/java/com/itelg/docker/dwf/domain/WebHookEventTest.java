package com.itelg.docker.dwf.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.itelg.docker.dwf.DomainTestSupport;

public class WebHookEventTest implements DomainTestSupport
{
    @Test
    public void testCopyConstructor()
    {
        WebHookEvent webHookEvent = new WebHookEvent(getCompleteWebHookEvent());
        assertEquals("jeggers", webHookEvent.getNamespace());
        assertEquals("dockerhub-webhook-forwarder", webHookEvent.getRepositoryName());
        assertEquals("latest", webHookEvent.getTag());
        assertEquals("jeggers/dockerhub-webhook-forwarder:latest", webHookEvent.getImage());
        assertEquals(getWebHookEventJson(), webHookEvent.getOriginalJson());
    }

    @Test
    public void testRemoveOriginalJson()
    {
        WebHookEvent webHookEventWithoutJson = new WebHookEvent(getCompleteWebHookEvent()).removeOriginalJson();
        assertEquals("jeggers", webHookEventWithoutJson.getNamespace());
        assertEquals("dockerhub-webhook-forwarder", webHookEventWithoutJson.getRepositoryName());
        assertEquals("latest", webHookEventWithoutJson.getTag());
        assertEquals("jeggers/dockerhub-webhook-forwarder:latest", webHookEventWithoutJson.getImage());
        assertNull(webHookEventWithoutJson.getOriginalJson());
    }

    @Test
    public void testToString()
    {
        assertEquals("WebHookEvent(namespace=jeggers, repositoryName=dockerhub-webhook-forwarder, tag=latest, image=jeggers/dockerhub-webhook-forwarder:latest)", getBaseWebHookEvent().toString());
        assertEquals("WebHookEvent(namespace=jeggers, repositoryName=dockerhub-webhook-forwarder, tag=latest, image=jeggers/dockerhub-webhook-forwarder:latest)", getCompleteWebHookEvent().toString());
    }
}