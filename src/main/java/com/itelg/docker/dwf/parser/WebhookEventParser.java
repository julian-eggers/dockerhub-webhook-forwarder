package com.itelg.docker.dwf.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.itelg.docker.dwf.domain.WebhookEvent;

@Component
public class WebhookEventParser extends AbstractJsonConverter<WebhookEvent>
{
    @Override
    public WebhookEvent convertJson(String json) throws JSONException
    {
        JSONObject object = new JSONObject(json);
        WebhookEvent event = new WebhookEvent();
        event.setNamespace(object.getJSONObject("repository").getString("namespace"));
        event.setRepositoryName(object.getJSONObject("repository").getString("name"));
        event.setTag(object.getJSONObject("push_data").getString("tag"));
        event.setImage(event.getNamespace() + "/" + event.getRepositoryName() + ":" + event.getTag());
        event.setOriginalJson(json);

        return event;
    }
}