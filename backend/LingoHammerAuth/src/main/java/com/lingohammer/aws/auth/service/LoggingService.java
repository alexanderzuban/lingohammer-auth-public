package com.lingohammer.aws.auth.service;

import static java.util.Optional.ofNullable;

public class LoggingService {
    public static final LoggingService LOG = new LoggingService();
    private TargetLog targetLog;

    private LoggingService() {
    }

    public void withLoggable(TargetLog logger) {
        this.targetLog = logger;
    }

    public void log(String message) {
        ofNullable(this.targetLog).ifPresent(logger -> logger.log(message));
    }


    public interface TargetLog {
        void log(String message);

        void log(byte[] message);
    }
}
