package com.itelg.docker.dwf.rest;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.dwf.domain.WebhookEvent;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.rest.domain.AccessDeniedException;
import com.itelg.docker.dwf.service.WebhookEventService;

@RestController
public class WebhookEventRestController
{
    @Autowired
    private WebhookEventService webhookEventService;

    @Autowired
    private WebhookEventParser webhookEventParser;

    @Autowired
    private Environment env;

    private String token;

    @PostConstruct
    public void init()
    {
        token = env.getProperty("request.token").replace("null", "");
    }

    void valideToken(String requestToken)
    {
        if (!StringUtils.isEmpty(token))
        {
            if (!token.equals(requestToken))
            {
                throw new AccessDeniedException();
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public WebhookEvent receive(@RequestParam(required = false) String token,
            @RequestBody String json)
    {
        valideToken(token);
        WebhookEvent event = webhookEventParser.parse(json);
        webhookEventService.publishEvent(event);
        return event;
    }

    @RequestMapping(value = "force", method = RequestMethod.GET)
    public WebhookEvent force(@RequestParam(required = false) String token,
            @RequestParam String namespace,
            @RequestParam String repository,
            @RequestParam String tag)
    {
        valideToken(token);
        WebhookEvent event = new WebhookEvent();
        event.setNamespace(namespace);
        event.setRepositoryName(repository);
        event.setTag(tag);
        event.setImage(event.getNamespace() + "/" + event.getRepositoryName() + ":" + event.getTag());
        webhookEventService.publishEvent(event);
        return event;
    }
}