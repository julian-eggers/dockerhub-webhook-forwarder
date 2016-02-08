package com.itelg.docker.dwf.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.service.WebhookEventService;

@RestController
public class WebhookEventRestController
{
    @Autowired
    private WebhookEventService webhookEventService;
    
    @Autowired
    private WebhookEventParser webhookEventParser;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public WebhookEvent receive(@RequestBody String json)
    {
        WebhookEvent event = webhookEventParser.parse(json);
        webhookEventService.publishEvent(event);
        return event;
    }
    
    @RequestMapping(value = "force", method = RequestMethod.GET)
    public WebhookEvent force(@RequestParam String namespace, @RequestParam String repository, @RequestParam String tag)
    {
        WebhookEvent event = new WebhookEvent();
        event.setNamespace(namespace);
        event.setRepository(repository);
        event.setTag(tag);
        webhookEventService.publishEvent(event);
        return event;
    }
}