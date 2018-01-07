package com.itelg.docker.dwf;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.dwf.domain.WebHookEvent;

import lombok.SneakyThrows;

public interface DomainTestSupport
{
    @SneakyThrows
    default String getWebHookEventJson()
    {
        return IOUtils.toString(new ClassPathResource("webhookevent.json").getInputStream(), Charset.defaultCharset());
    }

    default WebHookEvent getBaseWebHookEvent()
    {
        WebHookEvent webHookEvent = new WebHookEvent();
        webHookEvent.setNamespace("jeggers");
        webHookEvent.setRepositoryName("dockerhub-webhook-forwarder");
        webHookEvent.setTag("latest");
        webHookEvent.setImage("jeggers/dockerhub-webhook-forwarder:latest");
        return webHookEvent;
    }

    @SneakyThrows
    default WebHookEvent getCompleteWebHookEvent()
    {
        WebHookEvent webHookEvent = getBaseWebHookEvent();
        webHookEvent.setOriginalJson(getWebHookEventJson());
        return webHookEvent;
    }
}
