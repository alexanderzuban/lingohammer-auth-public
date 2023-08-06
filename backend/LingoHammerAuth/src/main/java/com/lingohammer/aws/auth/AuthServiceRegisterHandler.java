package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.RegisterRequest;
import com.lingohammer.aws.auth.data.RegisterResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;

import java.util.Map;

public class AuthServiceRegisterHandler extends AuthServiceBase {

    public AuthServiceRegisterHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceRegisterHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<RegisterRequest, RegisterResponse>(RegisterResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(RegisterRequest.class, request) //
                .onInputAndPayload(this::checkUserExists)//
                .onInputAndPayload(this::register)//
                .done();
    }

    private void checkUserExists(RegisterRequest request, RegisterResponse response) {
        var user = userService.checkUserExists(request.getEmail(), true);
        if (user) {
            response.fail(400, "User already exists", RegisterResponse.ERROR_CODE_USER_ALREADY_EXISTS);
        }
    }

    private void register(RegisterRequest input, RegisterResponse payload) {
        Map<String, String> customAttributes = Map.of(
                "firstName", input.getFirstName(),
                "lastName", input.getLastName()
        );

        var result = userService.registerUser(input.getEmail(), input.getPassword(), customAttributes);

        if (!result.isSuccess()) {
            payload.fail(400, result.getErrorMessage());
        }
    }
}
