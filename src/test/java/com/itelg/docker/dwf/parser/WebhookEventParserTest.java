package com.itelg.docker.dwf.parser;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.dwf.domain.WebhookEvent;

public class WebhookEventParserTest
{
    @Test
    public void testParse() throws Exception
    {
        String json = IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream(), Charset.forName("UTF-8"));
        WebhookEvent event = new WebhookEventParser().convert(json);
        Assert.assertEquals("jeggers", event.getNamespace());
        Assert.assertEquals("dockerhub-webhook-forwarder", event.getRepositoryName());
        Assert.assertEquals("latest", event.getTag());
        Assert.assertEquals("jeggers/dockerhub-webhook-forwarder:latest", event.getImage());
        Assert.assertNotNull(event.getOriginalJson());
    }
}