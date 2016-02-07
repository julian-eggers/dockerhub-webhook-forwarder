package com.itelg.docker.dwf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.thoughtworks.xstream.XStream;

@Configuration
public class MarshallingConfiguration
{
    @Bean
    public XStreamMarshaller xStreamMarshaller()
    {
        XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        xstreamMarshaller.setAutodetectAnnotations(true);
        xstreamMarshaller.setMode(XStream.NO_REFERENCES);
        xstreamMarshaller.setAnnotatedClasses(WebhookEvent.class);
        return xstreamMarshaller;
    }
}