package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.LoginRequest;
import com.lingohammer.aws.auth.data.LoginResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;

public class AuthServiceLoginHandler extends AuthServiceBase {

    public AuthServiceLoginHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceLoginHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<LoginRequest, LoginResponse>(LoginResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(LoginRequest.class, request) //
                .onInputAndPayload(this::checkUserExists)//
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
