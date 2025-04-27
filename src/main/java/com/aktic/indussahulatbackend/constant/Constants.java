package com.aktic.indussahulatbackend.constant;

public class Constants {

    // API Paths to bypass authentication in the filter
    public static final String[] FILTER_BYPASS_PATHS = {
            "/api/auth",
    };

    // White-listed URLs that do not require authentication
    public static final String[] WHITE_LIST_URL = {
            "/api/auth/**",
            "/api/plan/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
    };

    public static final int EVENT_AMBULANCE_ASSIGNMENT_TTL = 20;
    public static final int EVENT_HOSPITAL_ASSIGNMENT_TTL = 20;

    public static final long MAX_ACTIVE_INCIDENT_EVENTS = 10_000;
    public static final double DISTANCE_THRESHOLD_METERS = 500;

}
