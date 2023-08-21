package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

public abstract class AuthRequestProcess<I, O extends IsFlawed> {
    protected final ConfigurationService configurationService;
    protected final UserService userService;
    protected final LoggingService logger;

    protected AuthRequestProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        this.configurationService = configurationService;
        this.userService = userService;
        this.logger = logger;
    }

    public abstract void process(ResponseBuilder<I, O> response);


    protected LoggingService log() {
        return logger;
    }
}
