package com.aktic.indussahulatbackend.exception.customexceptions;

public class AmbulanceNotFoundException extends RuntimeException
{
    public static final String DEFAULT_MESSAGE = "Ambulance Not Found";

    public AmbulanceNotFoundException(String message)
    {
        super(message);
    }
}
