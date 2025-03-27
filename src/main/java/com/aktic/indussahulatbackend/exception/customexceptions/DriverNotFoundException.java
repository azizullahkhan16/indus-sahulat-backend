package com.aktic.indussahulatbackend.exception.customexceptions;

public class DriverNotFoundException extends RuntimeException
{
    public static final String DEFAULT_MESSAGE = "Driver Not Found";

    public DriverNotFoundException(String message)
    {
        super(message);
    }
}
