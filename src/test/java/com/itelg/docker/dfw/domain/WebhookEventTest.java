package com.itelg.docker.dfw.domain;

import org.junit.Assert;
import org.junit.Test;

import com.itelg.docker.dwf.domain.WebhookEvent;

public class WebhookEventTest
{
    @Test
    public void testToString()
    {
        Assert.assertTrue(new WebhookEvent().toString().startsWith("WebhookEvent"));
    }
}