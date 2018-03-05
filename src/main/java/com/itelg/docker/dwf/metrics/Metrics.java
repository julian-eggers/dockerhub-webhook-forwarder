package com.itelg.docker.dwf.metrics;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Metrics
{
    public static final String REST_AUTHENTICATION_FAILED_TOTAL_COUNT = "rest_authentication_failed_total_count";

    public static final String EVENT_INBOUND_TOTAL_COUNT = "event_inbound_total_count";

    public static final String EVENT_INBOUND_BYSOURCE_COUNT = "event_inbound_bysource_count";

    public static final String EVENT_INBOUND_LAST_TIMESTAMP = "event_inbound_last_timestamp";

    public static final String FORWARDEDTO_TOTAL_COUNT = "event_forwardedto_total_count";

    public static final String FORWARDEDTO_BYTARGET_COUNT = "event_forwardedto_bytarget_count";
}
