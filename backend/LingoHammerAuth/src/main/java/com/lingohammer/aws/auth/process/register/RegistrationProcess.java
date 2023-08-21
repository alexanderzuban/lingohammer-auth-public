package com.lingohammer.aws.auth.process.register;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;
import java.util.Map;

public class RegistrationProcess extends AuthRequestProcess<RegistrationRequest, RegistrationResponse> {

    @Inject
    public RegistrationProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
    }

    private void checkUserExists(RegistrationRequest request, RegistrationResponse response) {
        var user = userService.checkUserExists(request.getEmail(), true);
        if (user) {
            response.fail(400, "User already exists", RegistrationResponse.ERROR_CODE_USER_ALREADY_EXISTS);
        }
    }

    private void register(RegistrationRequest input, RegistrationResponse payload) {
        Map<String, String> customAttributes = Map.of(
                "firstName", input.getFirstName(),
                "lastName", input.getLastName()
        );

        var result = userService.registerUser(input.getEmail(), input.getPassword(), customAttributes);

        if (!result.isSuccess()) {
            payload.fail(400, result.getErrorMessage());
        }
    }

    @Override
    public void process(ResponseBuilder<RegistrationRequest, RegistrationResponse> response) {
        response.onInputAndPayload(this::checkUserExists)//
                .onInputAndPayload(this::register)//
                .done();
    }
}
