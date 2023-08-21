package com.lingohammer.aws.auth.service.user;

import com.lingohammer.aws.auth.AuthRuntimeException;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CognitoUserSignUp {
    private final ConfigurationService configurationService;
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
    private final LoggingService logger;


    public CognitoUserSignUp(ConfigurationService configurationService, CognitoIdentityProviderClient cognitoIdentityProviderClient, LoggingService logger) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
        this.configurationService = configurationService;
        this.logger = logger;
    }

    public IsFlawed registerUser(String email, String password, Map<String, String> customAttributes) {

        String generatedSecretHash = CognitoUserService.calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), email);

        ArrayList<AttributeType> attributes = generateAttributeTypes(customAttributes);

        if (checkIfUserPendingConfirmation(email)) {
            var disableUserResult = disableUser(email);
            if (!disableUserResult.isSuccess()) {
                return disableUserResult;
            }


            var deleteUserResult = deleteUser(email);
            if (!deleteUserResult.isSuccess()) {
                return deleteUserResult;
            }
        }

        var emailAttribute = AttributeType.builder()
                .name("email").value(email).build();
        attributes.add(emailAttribute);


        var signUpRequest = SignUpRequest.builder()
                .username(email)
                .password(password)
                .userAttributes(attributes)
                .clientId(configurationService.getCognitoUserPoolClientId())
                .secretHash(generatedSecretHash)
                .build();

        var signupResponse = cognitoIdentityProviderClient.signUp(signUpRequest);

        var result = new IsFlawed();
        result.setSuccess(signupResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(signupResponse.sdkHttpResponse().statusCode());

        return result;
    }


    private IsFlawed deleteUser(String email) {
        var adminDeleteUserRequest = AdminDeleteUserRequest.builder()
                .username(email)
                .userPoolId(configurationService.getCognitoUserPoolId())
                .build();

        var adminDeleteUserResponse = cognitoIdentityProviderClient.adminDeleteUser(adminDeleteUserRequest);

        var result = new IsFlawed();
        result.setSuccess(adminDeleteUserResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(adminDeleteUserResponse.sdkHttpResponse().statusCode());

        return result;
    }

    private IsFlawed disableUser(String email) {
        var adminDisableUserRequest = AdminDisableUserRequest.builder()
                .username(email)
                .userPoolId(configurationService.getCognitoUserPoolId())
                .build();

        var adminDisableUserResponse = cognitoIdentityProviderClient.adminDisableUser(adminDisableUserRequest);

        var result = new IsFlawed();
        result.setSuccess(adminDisableUserResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(adminDisableUserResponse.sdkHttpResponse().statusCode());

        return result;
    }


    private static ArrayList<AttributeType> generateAttributeTypes(Map<String, String> customAttributes) {
        var name = Objects.requireNonNullElse(customAttributes.get("firstName"), "") //
                + "  "  //
                + Objects.requireNonNullElse(customAttributes.get("lastName"), "");
        var nameAttribute = AttributeType.builder()
                .name("name")
                .value(name.trim())
                .build();


        var attributes = new ArrayList<AttributeType>();
        attributes.add(nameAttribute);


        customAttributes.entrySet()//
                .stream()//
                .filter(e -> e.getValue() != null)//
                .map(e ->
                        AttributeType.builder()
                                .name("custom:" + e.getKey())
                                .value(e.getValue())
                                .build()
                )//
                .forEach(attributes::add);
        return attributes;
    }

    private boolean checkIfUserPendingConfirmation(String email) {
        //check cognito for user with email
        //if user exists and is pending confirmation return true

        var adminGetUserRequest = AdminGetUserRequest.builder()
                .username(email)
                .userPoolId(configurationService.getCognitoUserPoolId())
                .build();

        try {
            var adminGetUserResponse = cognitoIdentityProviderClient.adminGetUser(adminGetUserRequest);
            if (adminGetUserResponse.sdkHttpResponse().isSuccessful()) {

                List<AttributeType> attributes = adminGetUserResponse.userAttributes();
                for (AttributeType attribute : attributes) {
                    if (attribute.name().equals("email_verified") && attribute.value().equals("false")) {
                        return true;
                    }
                }
                throw new AuthRuntimeException("User exists but is not pending confirmation");
            }
        } catch (UserNotFoundException e) {
            logger.log(e.getMessage());
        }
        return false;
    }


}
