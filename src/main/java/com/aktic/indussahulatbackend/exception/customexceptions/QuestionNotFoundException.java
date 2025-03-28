package com.aktic.indussahulatbackend.exception.customexceptions;

public class QuestionNotFoundException extends RuntimeException
{
    public static final String DEFAULT_MESSAGE = "Question Not Found";

    public QuestionNotFoundException(String message)
    {
        super(message);
    }
}
