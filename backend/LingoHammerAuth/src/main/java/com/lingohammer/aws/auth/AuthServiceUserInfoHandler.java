package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.UserInfoRequest;
import com.lingohammer.aws.auth.data.UserInfoResponse;
import com.lingohammer.aws.auth.service.CognitoUserService;
import com.lingohammer.aws.auth.service.ConfigurationService;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;

public class AuthServiceUserInfoHandler extends AuthServiceBase {

    public AuthServiceUserInfoHandler() {
        configurationService = new ConfigurationService();
        userService = new CognitoUserService(this.configurationService);
    }

    public AuthServiceUserInfoHandler(IConfigurationService configurationService, IUserService userService) {
        this.configurationService = configurationService;
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new ResponseBuilder<UserInfoRequest, UserInfoResponse>(UserInfoResponse::new);

        return result //
                .withLogger(context.getLogger()) //
                .withRequest(UserInfoRequest.class, request) //
                .onInputAndPayload(this::refreshToken)//
                .onInputAndPayload(this::getUserInfo)//
                .done();
    }

    private void getUserInfo(UserInfoRequest userInfoRequest, UserInfoResponse userInfoResponse) {
        var userInfo = userService.getUser(userInfoResponse.getToken().getAccessToken());
        userInfoResponse.setUser(userInfo);

        if (!userInfoResponse.isSuccess()) {
            userInfoResponse.setToken(null);
            userInfoResponse.setUser(null);
            userInfoResponse.fail(400, userInfoResponse.getErrorMessage(), UserInfoResponse.ERROR_CODE_INVALID_USER);
        }
    }

    private void refreshToken(UserInfoRequest userInfoRequest, UserInfoResponse userInfoResponse) {
        var securityToken = userService.refreshToken(userInfoRequest.getUserIdentifier(), userInfoRequest.getRefreshToken());
        userInfoResponse.setToken(securityToken);

        if (!securityToken.isSuccess()) {
            userInfoResponse.setToken(null);
            userInfoResponse.fail(400, securityToken.getErrorMessage(), UserInfoResponse.ERROR_CODE_INVALID_REFRESH_TOKEN);
        }
    }
}
