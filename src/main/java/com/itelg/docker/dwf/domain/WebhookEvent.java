package com.itelg.docker.dwf.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("webhookEvent")
public class WebhookEvent
{
    @XStreamAsAttribute
    private String namespace;
    
    @XStreamAsAttribute
    private String repository;
    
    @XStreamAsAttribute
    private String tag;

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public String getRepository()
    {
        return repository;
    }

    public void setRepository(String repository)
    {
        this.repository = repository;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    @Override
    public String toString()
    {
        return "WebhookEvent [namespace=" + namespace + ", repository=" + repository + ", tag=" + tag + "]";
    }
}