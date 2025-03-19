package com.aktic.indussahulatbackend.exception.customexceptions;

public class PatientNotFoundException extends RuntimeException
{
    public static final String DEFAULT_MESSAGE = "Patient Not Found";

    public PatientNotFoundException(String message)
    {
        super(message);
    }
}