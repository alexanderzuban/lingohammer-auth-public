package com.lingohammer.aws.auth.service.log;

import java.util.logging.Logger;

public class ConsoleLoggingService implements LoggingService {
    private static final Logger logger = Logger.getLogger("AuthenticationService");

    @Override
    public void log(String message) {
        logger.info(message);
    }

    @Override
    public void debug(String message) {
        logger.fine(message);
    }

    @Override
    public void error(String message, Throwable ex) {
        logger.severe(message);
        logger.severe(ex.getMessage());
    }
}
