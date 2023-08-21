package com.lingohammer.aws.auth.process.register;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;

public class RegistrationConfirmProcess extends AuthRequestProcess<RegistrationConfirmRequest, RegistrationConfirmResponse> {

    @Inject
    public RegistrationConfirmProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
    }

    private void confirmRegistrationCode(RegistrationConfirmRequest input, RegistrationConfirmResponse payload) {
        var securityToken = userService.confirmUserRegistration(input.getEmail(), input.getConfirmationCode());
        payload.setToken(securityToken);
        if (!securityToken.isSuccess()) {
            payload.fail(400, securityToken.getErrorMessage());
        }
    }

    private void login(RegistrationConfirmRequest input, RegistrationConfirmResponse payload) {
        try {
            //try to execute login
            var securityToken = userService.login(input.getEmail(), input.getPassword());

            if (securityToken.isSuccess()) {
                var userInfo = userService.getUser(securityToken.getAccessToken());
                payload.setToken(securityToken);
                payload.setUser(userInfo);
            }
        } catch (Exception e) {
            //do not fail confirm if login fails
        }
    }

    @Override
    public void process(ResponseBuilder<RegistrationConfirmRequest, RegistrationConfirmResponse> response) {
        response.onInputAndPayload(this::confirmRegistrationCode)//
                .onInputAndPayload(this::login)//login after registration
                .done();
    }
}
