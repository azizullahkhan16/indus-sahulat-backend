package com.aktic.indussahulatbackend.constant;

public enum SocketEndpoint {
    ACTIVE_INCIDENT_EVENT("/topic/ambulance-provider/active-incident/"),
    INCIDENT_EVENT_UPDATE("/user/event/"),
    INCIDENT_EVENT_LIVE_LOCATION("/user/event/live-location/"),
    NEW_ADMIT_REQUEST("/topic/hospital/admit-request/"),
    USER_NOTIFICATION("/user/notification/"),
    AMBULANCE_ASSIGNMENT_TO_DRIVER("/user/assignment/"),
    SEND_MESSAGE_TO_CHATROOM("/topic/event/chatroom/");

    private final String path;

    SocketEndpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
