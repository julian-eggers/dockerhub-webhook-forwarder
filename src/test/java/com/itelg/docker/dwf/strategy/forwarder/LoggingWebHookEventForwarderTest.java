package com.itelg.docker.dwf.strategy.forwarder;

import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.itelg.docker.dwf.DomainTestSupport;

@RunWith(PowerMockRunner.class)
public class LoggingWebHookEventForwarderTest implements DomainTestSupport
{
    private WebHookEventForwarder webHookEventForwarder = new LoggingWebHookEventForwarder();

    @Test
    public void testPublish()
    {
        replayAll();
        webHookEventForwarder.publish(getCompleteWebHookEvent());
        verifyAll();
    }
}
