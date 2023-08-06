package com.lingohammer.aws.auth.service;

import com.lingohammer.aws.auth.AuthRuntimeException;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CognitoUserService implements IUserService {

    private final IConfigurationService configurationService;
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public CognitoUserService(IConfigurationService configurationService) {
        this(configurationService, CognitoIdentityProviderClient.builder()
                .region(Region.of(configurationService.getRegion()))
                .build());
    }

    public CognitoUserService(IConfigurationService configurationService, CognitoIdentityProviderClient cognitoIdentityProviderClient) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
        this.configurationService = configurationService;
    }


    @Override
    public IsFlawed confirmRestorePassword(String email, String confirmationCode, String newPassword) {
        var generatedSecretHash = calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), email);

        var confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                .clientId(configurationService.getCognitoUserPoolClientId())
                .secretHash(generatedSecretHash)
                .username(email)
                .password(newPassword)
                .confirmationCode(confirmationCode)
                .build();

        var confirmForgotPasswordResponse = cognitoIdentityProviderClient.confirmForgotPassword(confirmForgotPasswordRequest);

        var result = new IsFlawed();
        result.setSuccess(confirmForgotPasswordResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(confirmForgotPasswordResponse.sdkHttpResponse().statusCode());
        result.setErrorMessage(confirmForgotPasswordResponse.sdkHttpResponse().statusText().orElse(null));

        return result;
    }

    @Override
    public IsFlawed requestPasswordRestoration(String email) {
        var generatedSecretHash = calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), email);

        var forgotPasswordRequest = ForgotPasswordRequest.builder()
                .clientId(configurationService.getCognitoUserPoolClientId())
                .secretHash(generatedSecretHash)
                .username(email)
                .build();

        var forgotPasswordResponse = cognitoIdentityProviderClient.forgotPassword(forgotPasswordRequest);

        var result = new IsFlawed();
        result.setSuccess(forgotPasswordResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(forgotPasswordResponse.sdkHttpResponse().statusCode());
        result.setErrorMessage(forgotPasswordResponse.sdkHttpResponse().statusText().orElse(null));

        return result;
    }

    @Override
    public IsFlawed registerUser(String email, String password, Map<String, String> customAttributes) {
        return new CognitoUserSignUp(configurationService, cognitoIdentityProviderClient)
                .registerUser(email, password, customAttributes);
    }

    @Override
    public UserInfo getUser(String accessToken) {
        var getUserRequest = GetUserRequest //
                .builder() //
                .accessToken(accessToken) //
                .build();
        var getUserResponse = cognitoIdentityProviderClient.getUser(getUserRequest);

        var result = new UserInfo();
        result.setSuccess(getUserResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(getUserResponse.sdkHttpResponse().statusCode());
        result.setEmail(getUserResponse.getValueForField("email", String.class).orElse(null));
        result.setUserIdentifier(getUserResponse.username());

        var userAttributes = getUserResponse.userAttributes();
        userAttributes.forEach(attribute ->
                result.addAttribute(formatAttributeName(attribute.name()), attribute.value())
        );

        return result;
    }

    private String formatAttributeName(String name) {
        if (name != null && name.startsWith("custom:")) {
            return name.substring(7);
        }
        return name;
    }

    @Override
    public boolean checkUserExists(String email, boolean verifyEmail) {
        var adminGetUserRequest = AdminGetUserRequest.builder()
                .username(email)
                .userPoolId(configurationService.getCognitoUserPoolId())
                .build();

        try {
            var adminGetUserResponse = cognitoIdentityProviderClient.adminGetUser(adminGetUserRequest);
            if (adminGetUserResponse.sdkHttpResponse().isSuccessful()) {
                if (verifyEmail) {
                    List<AttributeType> attributes = adminGetUserResponse.userAttributes();
                    for (AttributeType attribute : attributes) {
                        if (attribute.name().equals("email_verified") && attribute.value().equals("true")) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
        } catch (UserNotFoundException e) {
            LoggingService.LOG.log(e.getMessage());
        }
        return false;
    }

    @Override
    public SecurityToken confirmUserRegistration(String email, String confirmationCode) {

        var generatedSecretHash = calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), email);

        var confirmSignUpRequest = ConfirmSignUpRequest.builder()
                .secretHash(generatedSecretHash)
                .username(email)
                .confirmationCode(confirmationCode)
                .clientId(configurationService.getCognitoUserPoolClientId())
                .build();

        var confirmSignUpResponse = cognitoIdentityProviderClient.confirmSignUp(confirmSignUpRequest);

        var result = new SecurityToken();
        result.setSuccess(confirmSignUpResponse.sdkHttpResponse().isSuccessful());
        result.setLoggedIn(false);
        result.setStatusCode(confirmSignUpResponse.sdkHttpResponse().statusCode());
        result.setRequiresConfirmation(!confirmSignUpResponse.sdkHttpResponse().isSuccessful());

        return result;
    }

    @Override
    public SecurityToken login(String email, String password) {

        var generatedSecretHash = calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), email);
        var authParams = new HashMap<String, String>();

        authParams.put("USERNAME", email);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", generatedSecretHash);


        var initiateAuthRequest = InitiateAuthRequest.builder()
                .clientId(configurationService.getCognitoUserPoolClientId())
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .authParameters(authParams)
                .build();

        return getSecurityToken(initiateAuthRequest);
    }

    @Override
    public SecurityToken refreshToken(String userId, String refreshToken) {

        var generatedSecretHash = calculateSecretHash(configurationService.getCognitoUserPoolClientId(), configurationService.getCognitoUserPoolClientSecret(), userId);

        var authParams = new HashMap<String, String>();
        authParams.put("REFRESH_TOKEN", refreshToken);
        authParams.put("SECRET_HASH", generatedSecretHash);

        var initiateAuthRequest = InitiateAuthRequest.builder()
                .clientId(configurationService.getCognitoUserPoolClientId())
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(authParams)
                .build();

        return getSecurityToken(initiateAuthRequest);
    }

    private SecurityToken getSecurityToken(InitiateAuthRequest initiateAuthRequest) {
        var initiateAuthResponse = cognitoIdentityProviderClient.initiateAuth(initiateAuthRequest);
        var authenticationResultType = initiateAuthResponse.authenticationResult();

        var result = new SecurityToken();
        result.setAccessToken(authenticationResultType.accessToken());
        result.setRefreshToken(authenticationResultType.refreshToken());
        result.setToken(authenticationResultType.idToken());
        result.setSuccess(initiateAuthResponse.sdkHttpResponse().isSuccessful());
        result.setStatusCode(initiateAuthResponse.sdkHttpResponse().statusCode());
        result.setLoggedIn(initiateAuthResponse.sdkHttpResponse().isSuccessful());
        if (initiateAuthResponse.authenticationResult().expiresIn() != null) {
            var expiresIn = initiateAuthResponse.authenticationResult().expiresIn();
            var expiresAt = new Date(System.currentTimeMillis() + expiresIn * 1000);
            result.setExpiresAt(expiresAt);
        }
        return result;
    }


    static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String email) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        var signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            var mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(email.getBytes(StandardCharsets.UTF_8));
            var rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new AuthRuntimeException("Error while calculating SecretHash");
        }
    }


}
