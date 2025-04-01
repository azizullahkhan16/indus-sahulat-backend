package com.aktic.indussahulatbackend.exception.customexceptions;

public class AmbulanceAssignmentNotFoundException extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "Ambulance Assignment Not Found";

    public AmbulanceAssignmentNotFoundException(String message)
    {
        super(message);
    }
}
