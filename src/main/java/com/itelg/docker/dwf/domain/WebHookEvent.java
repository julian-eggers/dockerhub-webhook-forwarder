package com.itelg.docker.dwf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "originalJson")
public class WebHookEvent
{
    private String namespace;

    private String repositoryName;

    private String tag;

    private String image;

    @JsonIgnore
    private String originalJson;

    public WebHookEvent(WebHookEvent webHookEvent)
    {
        this.namespace = webHookEvent.getNamespace();
        this.repositoryName = webHookEvent.getRepositoryName();
        this.tag = webHookEvent.getTag();
        this.image = webHookEvent.getImage();
        this.originalJson = webHookEvent.getOriginalJson();
    }

    public WebHookEvent removeOriginalJson()
    {
        WebHookEvent webHookEvent = new WebHookEvent(this);
        webHookEvent.setOriginalJson(null);
        return webHookEvent;
    }
}