package com.itelg.docker.dwf.parser;

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
        String json = IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream());
        WebhookEvent event = new WebhookEventParser().parse(json);
        Assert.assertEquals("biscarch", event.getNamespace());
        Assert.assertEquals("biscarch/webhook-tester-repo", event.getRepository());
        Assert.assertEquals("newtag", event.getTag());
    }
}