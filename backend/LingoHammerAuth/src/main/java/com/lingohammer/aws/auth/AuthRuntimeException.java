package com.lingohammer.aws.auth;

public class AuthRuntimeException extends RuntimeException {
    public AuthRuntimeException(String message) {
        super(message);
    }

    public AuthRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthRuntimeException(Throwable cause) {
        super(cause);
    }
    
}
