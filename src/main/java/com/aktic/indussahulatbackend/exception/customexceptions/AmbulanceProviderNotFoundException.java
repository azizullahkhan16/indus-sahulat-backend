package com.aktic.indussahulatbackend.exception.customexceptions;

public class AmbulanceProviderNotFoundException extends RuntimeException
{
    public static final String DEFAULT_MESSAGE = "Ambulance Provider Not Found";

    public AmbulanceProviderNotFoundException(String message)
    {
        super(message);
    }
}
