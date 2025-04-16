package com.aktic.indussahulatbackend.constant;

public enum SocketEndpoint {
    INCIDENT_EVENT_UPDATE("/user/event/"),
    INCIDENT_EVENT_LIVE_LOCATION("/user/event/live-location/"),
    USER_NOTIFICATION("/user/notification/");

    private final String path;

    SocketEndpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
