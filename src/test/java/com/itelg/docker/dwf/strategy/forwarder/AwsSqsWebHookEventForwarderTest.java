package com.itelg.docker.dwf.strategy.forwarder;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Arrays;

import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itelg.docker.dwf.DomainTestSupport;
import com.itelg.docker.dwf.domain.WebHookEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RunWith(PowerMockRunner.class)
public class AwsSqsWebHookEventForwarderTest implements DomainTestSupport
{
    @TestSubject
    private WebHookEventForwarder webHookEventForwarder = new AwsSqsWebHookEventForwarder();

    @MockStrict
    private QueueMessagingTemplate queueMessagingTemplate;

    @MockStrict
    private ObjectMapper objectMapper;

    @MockStrict
    private MeterRegistry meterRegistry;

    @Test
    public void testPublish() throws Exception
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList("original1", "original2"));
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList("compressed1", "compressed2"));

        queueMessagingTemplate.send(eq("original1"), anyObject(Message.class));

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-original");
        expectLastCall().andReturn(mock(Counter.class));

        queueMessagingTemplate.send(eq("original2"), anyObject(Message.class));

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-original");
        expectLastCall().andReturn(mock(Counter.class));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed1"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed2"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }

    @Test
    public void testPublishWithoutOriginalJson() throws Exception
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList("original1", "original2"));
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList("compressed1", "compressed2"));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed1"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed2"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getBaseWebHookEvent());
        verifyAll();
    }

    @Test
    public void testPublishWithoutOriginalQueues() throws Exception
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList());
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList("compressed1", "compressed2"));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed1"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed2"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }

    @Test
    public void testPublishWithInvalidOriginalQueues()
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList("original1", ""));
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList());

        queueMessagingTemplate.send(eq("original1"), anyObject(Message.class));

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-original");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }

    @Test
    public void testPublishWithoutCompressedQueues()
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList("original1", "original2"));
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList());

        queueMessagingTemplate.send(eq("original1"), anyObject(Message.class));

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-original");
        expectLastCall().andReturn(mock(Counter.class));

        queueMessagingTemplate.send(eq("original2"), anyObject(Message.class));

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-original");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }

    @Test
    public void testPublishWithInvalidCompressedQueues() throws Exception
    {
        setInternalState(webHookEventForwarder, "originalQueues", Arrays.asList());
        setInternalState(webHookEventForwarder, "compressedQueues", Arrays.asList("compressed1", ""));

        objectMapper.writeValueAsString(anyObject(WebHookEvent.class));
        expectLastCall().andReturn(getCompressedWebHookEventJson());

        queueMessagingTemplate.send(eq("compressed1"), anyObject(Message.class));
        expectLastCall().andAnswer(() ->
        {
            @SuppressWarnings("unchecked")
            Message<String> message = (Message<String>) getCurrentArguments()[1];
            assertEquals(getCompressedWebHookEventJson(), message.getPayload());
            return null;
        });

        meterRegistry.counter("event_forwardedto_bytarget_count", "target", "awssqs-compressed");
        expectLastCall().andReturn(mock(Counter.class));

        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }
}
