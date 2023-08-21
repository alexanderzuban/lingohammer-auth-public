package com.lingohammer.aws.auth.process.restore;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;

public class RestorePasswordProcess extends AuthRequestProcess<RestorePasswordRequest, RestorePasswordResponse> {

    @Inject
    public RestorePasswordProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
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

    @Override
    public void process(ResponseBuilder<RestorePasswordRequest, RestorePasswordResponse> response) {
        response.onInputAndPayload(this::checkUserExists)//
                .onInputAndPayload(this::requestPasswordRestoration)//
                .done();
    }
}
