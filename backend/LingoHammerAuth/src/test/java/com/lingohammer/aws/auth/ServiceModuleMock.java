package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;

public class ServiceModuleMock {
    private UserService mockUserService;

    private LoggingService mockLogger;

    private ConfigurationService mockConfigurationService;


    public ServiceModuleMock(ConfigurationService mockConfigurationService, UserService mockUserService, LoggingService mockLogger) {
        this.mockUserService = mockUserService;
        this.mockLogger = mockLogger;
        this.mockConfigurationService = mockConfigurationService;
    }


    @Provides
    @Singleton
    public ConfigurationService configurationService() {
        return this.mockConfigurationService;
    }

    @Provides
    @Singleton
    public UserService userService() {
        return this.mockUserService;
    }

    @Provides
    @Singleton
    public LoggingService loggingService() {
        return this.mockLogger;
    }


}
