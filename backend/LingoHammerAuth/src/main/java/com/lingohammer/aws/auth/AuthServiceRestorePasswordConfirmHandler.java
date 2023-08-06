package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.RestorePasswordConfirmRequest;
import com.lingohammer.aws.auth.data.RestorePasswordConfirmResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;

public class AuthServiceRestorePasswordConfirmHandler extends AuthServiceBase {

    public AuthServiceRestorePasswordConfirmHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceRestorePasswordConfirmHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<RestorePasswordConfirmRequest, RestorePasswordConfirmResponse>(RestorePasswordConfirmResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(RestorePasswordConfirmRequest.class, request) //
                .onInputAndPayload(this::confirmPasswordChange)//
                .onInputAndPayload(this::login)//
                .done();
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
}
