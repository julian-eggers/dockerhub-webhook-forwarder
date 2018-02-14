package com.itelg.docker.dwf.metrics;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Metrics
{
    public static final String REST_AUTHENTICATION_FAILED_TOTAL = "rest_authentication_failed_total";

    public static final String EVENT_INBOUND_TOTAL_SUM = "event_inbound_total_sum";

    public static final String EVENT_INBOUND_BYSOURCE_SUM = "event_inbound_bysource_sum";

    public static final String EVENT_INBOUND_LAST_TIMESTAMP = "event_inbound_last_timestamp";

    public static final String FORWARDEDTO_TOTAL_SUM = "event_forwardedto_total_sum";

    public static final String FORWARDEDTO_BYTARGET_SUM = "event_forwardedto_bytarget_sum";
}
