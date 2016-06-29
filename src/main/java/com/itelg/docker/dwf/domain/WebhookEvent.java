package com.itelg.docker.dwf.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;

@XStreamAlias("webhookEvent")
@Data
public class WebhookEvent
{
    @XStreamAsAttribute
    private String namespace;

    @XStreamAsAttribute
    private String repositoryName;

    @XStreamAsAttribute
    private String tag;

    @XStreamAsAttribute
    private String image;
}