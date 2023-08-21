package com.lingohammer.aws.auth.process.restore;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;

public class RestorePasswordConfirmProcess extends AuthRequestProcess<RestorePasswordConfirmRequest, RestorePasswordConfirmResponse> {

    @Inject
    public RestorePasswordConfirmProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
    }

    private void login(RestorePasswordConfirmRequest request, RestorePasswordConfirmResponse response) {
        var securityToken = userService.login(request.getEmail(), request.getNewPassword());

        if (securityToken.isSuccess()) {
            var userInfo = userService.getUser(securityToken.getAccessToken());
            response.setToken(securityToken);
            response.setUser(userInfo);
        } else {
            response.fail(400, securityToken.getErrorMessage());
        }
    }

    private void confirmPasswordChange(RestorePasswordConfirmRequest request, RestorePasswordConfirmResponse response) {
        var result = userService.confirmRestorePassword(request.getEmail(), request.getConfirmationCode(), request.getNewPassword());

        response.setSuccess(result.isSuccess());

        if (!result.isSuccess()) {
            response.setErrorMessage(result.getErrorMessage());
            response.setStatusCode(400);
        }
    }

    @Override
    public void process(ResponseBuilder<RestorePasswordConfirmRequest, RestorePasswordConfirmResponse> response) {
        response.onInputAndPayload(this::confirmPasswordChange)//
                .onInputAndPayload(this::login)//login after registration
                .done();
    }
}
