package com.lingohammer.aws.auth.data;

public class IsFlawed {
    private boolean success = true;
    private int statusCode = 200;
    private Integer errorCode;
    private String errorMessage;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void fail(int statusCode, String errorMessage) {
        setSuccess(false);
        setStatusCode(statusCode);
        setErrorMessage(errorMessage);
    }

    public void fail(int statusCode, String errorMessage, int errorCode) {
        setSuccess(false);
        setStatusCode(statusCode);
        setErrorMessage(errorMessage);
        setErrorCode(errorCode);
    }
}
