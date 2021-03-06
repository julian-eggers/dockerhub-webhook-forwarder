package com.itelg.docker.dwf.parser;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.itelg.docker.dwf.domain.WebHookEvent;

@Component
public class WebhookEventParser
{
    public WebHookEvent parse(String json)
    {
        JSONObject object = new JSONObject(json);
        WebHookEvent event = new WebHookEvent();
        event.setNamespace(object.getJSONObject("repository").getString("namespace"));
        event.setRepositoryName(object.getJSONObject("repository").getString("name"));
        event.setTag(object.getJSONObject("push_data").getString("tag"));
        event.setImage(event.getNamespace() + "/" + event.getRepositoryName() + ":" + event.getTag());
        event.setOriginalJson(json);

        return event;
    }
}