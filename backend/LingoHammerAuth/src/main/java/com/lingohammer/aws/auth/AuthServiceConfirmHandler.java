package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.ConfirmRegistrationRequest;
import com.lingohammer.aws.auth.data.ConfirmRegistrationResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;


public class AuthServiceConfirmHandler extends AuthServiceBase {

    public AuthServiceConfirmHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceConfirmHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }


    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<ConfirmRegistrationRequest, ConfirmRegistrationResponse>(ConfirmRegistrationResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(ConfirmRegistrationRequest.class, request) //
                .onInputAndPayload(this::confirmRegistrationCode)//
                .onInputAndPayload(this::login)//
                .done();
    }

    private void confirmRegistrationCode(ConfirmRegistrationRequest input, ConfirmRegistrationResponse payload) {
        var securityToken = userService.confirmUserRegistration(input.getEmail(), input.getConfirmationCode());
        payload.setToken(securityToken);
        if (!securityToken.isSuccess()) {
            payload.fail(400, securityToken.getErrorMessage());
        }
    }

    private void login(ConfirmRegistrationRequest input, ConfirmRegistrationResponse payload) {
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
}
