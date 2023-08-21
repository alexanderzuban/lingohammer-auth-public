package com.lingohammer.aws.auth.process.login;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.AuthRuntimeException;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;


public class LoginProcess extends AuthRequestProcess<LoginRequest, LoginResponse> {

    @Inject
    public LoginProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
    }

    public void process(ResponseBuilder<LoginRequest, LoginResponse> response) {
        response.onInputAndPayload(this::checkUserExists)//
                .onInputAndPayload(this::login)//
                .done();
    }

    private void checkUserExists(LoginRequest request, LoginResponse loginResponse) {
        try {
            if (!userService.checkUserExists(request.getEmail(), true)) {
                throw new AuthRuntimeException("User does not exist or is not confirmed");
            }
        } catch (Exception e) {
            loginResponse.fail(401, e.getMessage(), LoginResponse.ERROR_CODE_MISSING_USER);
        }
    }

    private void login(LoginRequest input, LoginResponse payload) {
        try {
            var securityToken = userService.login(input.getEmail(), input.getPassword());

            if (!securityToken.isSuccess()) {
                throw new AuthRuntimeException(securityToken.getErrorMessage());
            }

            var userInfo = userService.getUser(securityToken.getAccessToken());
            payload.setToken(securityToken);
            payload.setUser(userInfo);
        } catch (Exception e) {
            payload.fail(401, e.getMessage(), LoginResponse.ERROR_CODE_WRONG_PASSWORD);
        }

    }
}
