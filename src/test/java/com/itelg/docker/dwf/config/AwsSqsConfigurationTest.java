package com.itelg.docker.dwf.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Test;

public class AwsSqsConfigurationTest
{
    @Test
    public void testInitWithoutAnyQueue()
    {
        try
        {
            AwsSqsConfiguration configuration = new AwsSqsConfiguration();
            configuration.setAccessKey("accessKey");
            configuration.setSecretKey("secretKey");
            configuration.setOriginalQueues(Collections.emptyList());
            configuration.setCompressedQueues(Collections.emptyList());
            configuration.init();
            fail("Exceptiom expected!");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("You have to specify at least one queue!", e.getMessage());
        }
    }

    @Test
    public void testInitWithOriginalQueue()
    {
        AwsSqsConfiguration configuration = new AwsSqsConfiguration();
        configuration.setAccessKey("accessKey");
        configuration.setSecretKey("secretKey");
        configuration.setOriginalQueues(Collections.singletonList("original-queue"));
        configuration.setCompressedQueues(Collections.emptyList());
        configuration.init();
    }

    @Test
    public void testInitWithCompressedQueue()
    {
        AwsSqsConfiguration configuration = new AwsSqsConfiguration();
        configuration.setAccessKey("accessKey");
        configuration.setSecretKey("secretKey");
        configuration.setOriginalQueues(Collections.emptyList());
        configuration.setCompressedQueues(Collections.singletonList("compressed-queue"));
        configuration.init();
    }

    @Test
    public void testInitWithOriginalAndCompressedQueue()
    {
        AwsSqsConfiguration configuration = new AwsSqsConfiguration();
        configuration.setAccessKey("accessKey");
        configuration.setSecretKey("secretKey");
        configuration.setOriginalQueues(Collections.singletonList("original-queue"));
        configuration.setCompressedQueues(Collections.singletonList("compressed-queue"));
        configuration.init();
    }

    @Test
    public void testAwsSqsAsyncWithStringRegion()
    {
        AwsSqsConfiguration configuration = new AwsSqsConfiguration();
        configuration.setAccessKey("accessKey");
        configuration.setSecretKey("secretKey");
        configuration.setRegion("eu-central-1");
        configuration.awsSqsAsync();
    }

    @Test
    public void testAwsSqsAsyncWithEnumRegion()
    {
        AwsSqsConfiguration configuration = new AwsSqsConfiguration();
        configuration.setAccessKey("accessKey");
        configuration.setSecretKey("secretKey");
        configuration.setRegion("EU_CENTRAL_1");
        configuration.awsSqsAsync();
    }

    @Test
    public void testAwsSqsAsyncWithUnknownRegion()
    {
        try
        {
            AwsSqsConfiguration configuration = new AwsSqsConfiguration();
            configuration.setAccessKey("accessKey");
            configuration.setSecretKey("secretKey");
            configuration.setRegion("eucentral1");
            configuration.awsSqsAsync();
            fail("Exceptiom expected!");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Cannot create enum from eucentral1 value!", e.getMessage());
        }
    }
}
