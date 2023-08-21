package com.lingohammer.aws.auth.service.log;

public interface LoggingService {

    void log(String message);

    void debug(String message);

    void error(String message, Throwable ex);
}
