package com.itelg.docker.dwf.strategy.forwarder;

import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Arrays;

import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

@RunWith(PowerMockRunner.class)
public class AwsSqsWebHookEventForwarderTest
{
    @TestSubject
    private WebHookEventForwarder webHookEventForwarder = new AwsSqsWebHookEventForwarder();

    @MockStrict
    private QueueMessagingTemplate queueMessagingTemplate;

    @Test
    public void before()
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList("original1", "original2"));
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList("compressed1", "compressed2"));
    }

    @Test
    public void testPublish()
    {

    }

    @Test
    public void testPublishWithoutOriginalQueues()
    {

    }

    @Test
    public void testPublishWithInvalidOriginalQueues()
    {

    }

    @Test
    public void testPublishWithoutCompressedQueues()
    {

    }

    @Test
    public void testPublishWithInvalidCompressedQueues()
    {

    }
}
