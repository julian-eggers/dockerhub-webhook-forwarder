package com.itelg.docker.dwf.parser;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.itelg.docker.dwf.domain.WebhookEvent;

@Component
public class WebhookEventParser
{
    public WebhookEvent parse(String json)
    {
        JSONObject object = new JSONObject(json);
        WebhookEvent event = new WebhookEvent();
        event.setNamespace(object.getJSONObject("repository").getString("namespace"));
        event.setRepository(object.getJSONObject("repository").getString("repo_name"));
        event.setTag(object.getJSONObject("push_data").getString("tag"));

        return event;
    }
}