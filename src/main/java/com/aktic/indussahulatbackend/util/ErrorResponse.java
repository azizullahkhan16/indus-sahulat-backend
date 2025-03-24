package com.aktic.indussahulatbackend.util;

import java.util.List;

public class ErrorResponse
{
    private String message;
    private boolean success;
    private List<String> data;

    public ErrorResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ErrorResponse(String message, boolean success, List<String> data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
