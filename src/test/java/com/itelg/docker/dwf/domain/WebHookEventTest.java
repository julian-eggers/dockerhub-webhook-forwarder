package com.itelg.docker.dwf.domain;

import org.junit.Assert;
import org.junit.Test;

import com.itelg.docker.dwf.domain.WebHookEvent;

public class WebHookEventTest
{
    @Test
    public void testToString()
    {
        Assert.assertTrue(new WebHookEvent().toString().startsWith("WebHookEvent"));
    }
}