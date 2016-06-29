package com.itelg.docker.dwf.domain;

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

    private String originalJson;
}