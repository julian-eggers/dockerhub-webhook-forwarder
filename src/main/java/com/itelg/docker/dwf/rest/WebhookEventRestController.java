package com.itelg.docker.dwf.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.dwf.domain.WebHookEvent;
import com.itelg.docker.dwf.metrics.Metrics;
import com.itelg.docker.dwf.parser.WebhookEventParser;
import com.itelg.docker.dwf.rest.domain.AccessDeniedException;
import com.itelg.docker.dwf.service.WebHookEventService;

@RestController
public class WebhookEventRestController
{
    @Autowired
    private WebHookEventService webHookEventService;

    @Autowired
    private WebhookEventParser webhookEventParser;

    @Autowired
    private CounterService counterService;

    @Value("${request.token}")
    private String token;

    void valideToken(String requestToken)
    {
        if (!StringUtils.isEmpty(token))
        {
            if (!token.equals(requestToken))
            {
                counterService.increment(Metrics.REST_AUTHENTICATION_FAILED_TOTAL);
                throw new AccessDeniedException();
            }
        }
    }

    @PostMapping
    public WebHookEvent receive(@RequestParam(required = false) String token, @RequestBody String json)
    {
        valideToken(token);
        WebHookEvent event = webhookEventParser.parse(json);
        webHookEventService.publishEvent(event);
        counterService.increment(Metrics.inbound("WebHookEvent"));
        return event;
    }

    @RequestMapping("force")
    public WebHookEvent force(@RequestParam(required = false) String token,
            @RequestParam String namespace,
            @RequestParam String repository,
            @RequestParam String tag)
    {
        valideToken(token);
        WebHookEvent event = new WebHookEvent();
        event.setNamespace(namespace);
        event.setRepositoryName(repository);
        event.setTag(tag);
        event.setImage(event.getNamespace() + "/" + event.getRepositoryName() + ":" + event.getTag());
        webHookEventService.publishEvent(event);
        counterService.increment(Metrics.inbound("force"));
        return event;
    }
}