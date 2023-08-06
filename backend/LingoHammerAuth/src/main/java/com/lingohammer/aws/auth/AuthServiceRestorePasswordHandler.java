package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.RestorePasswordRequest;
import com.lingohammer.aws.auth.data.RestorePasswordResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;

public class AuthServiceRestorePasswordHandler extends AuthServiceBase {

    public AuthServiceRestorePasswordHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceRestorePasswordHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<RestorePasswordRequest, RestorePasswordResponse>(RestorePasswordResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(RestorePasswordRequest.class, request) //
                .onInputAndPayload(this::checkUserExists)//
                .onInputAndPayload(this::requestPasswordRestoration)//
                .done();
    }

    private void requestPasswordRestoration(RestorePasswordRequest request, RestorePasswordResponse response) {
        var result = userService.requestPasswordRestoration(request.getEmail());

        if (!result.isSuccess()) {
            response.fail(400, result.getErrorMessage(), RestorePasswordResponse.ERROR_CODE_REQUEST_ERROR);
        }
    }

    private void checkUserExists(RestorePasswordRequest request, RestorePasswordResponse response) {
        if (!userService.checkUserExists(request.getEmail(), false)) {
            response.fail(400, "User does not exist", RestorePasswordResponse.ERROR_CODE_MISSING_USER);
        }
    }

}
