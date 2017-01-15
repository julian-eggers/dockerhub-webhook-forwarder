package com.itelg.docker.dwf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "originalJson")
public class WebhookEvent
{
    private String namespace;

    private String repositoryName;

    private String tag;

    private String image;

    @JsonIgnore
    private String originalJson;
}