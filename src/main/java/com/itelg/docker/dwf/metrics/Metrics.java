package com.itelg.docker.dwf.metrics;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Metrics
{
    public static final String REST_AUTHENTICATION_FAILED_TOTAL = "rest_authentication_failed_total";

    public static final String EVENT_INBOUND_TOTAL = "event_inbound_total";

    public static final String EVENT_INBOUND_LAST_TIMESTAMP = "event_inbound_last_timestamp";

    public static final String FORWARDED_TO_TOTAL = "event_forwarded_total";

    public String inbound(String source)
    {
        return "event_inbound{source=" + source + "}";
    }

    public String forwardedTo(String target)
    {
        return "event_forwarded_to{target=" + target + "}";
    }
}
