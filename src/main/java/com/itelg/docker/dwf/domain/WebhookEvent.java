package com.itelg.docker.dwf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
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