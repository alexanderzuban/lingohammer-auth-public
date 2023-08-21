package com.lingohammer.aws.auth.service;

import com.lingohammer.aws.auth.service.configuration.CloudConfigurationService;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.ConsoleLoggingService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.CognitoUserService;
import com.lingohammer.aws.auth.service.user.UserService;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;


public class ServicesModule {

    @Provides
    @Singleton
    public ConfigurationService configurationService(CloudConfigurationService impl) {
        return impl;
    }

    @Provides
    @Singleton
    public UserService userService(CognitoUserService impl) {
        return impl;
    }

    @Provides
    @Singleton
    public LoggingService loggingService(ConsoleLoggingService impl) {
        return impl;
    }


}
