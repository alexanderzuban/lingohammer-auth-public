package com.lingohammer.aws.auth.process.info;

import com.lingohammer.aws.auth.AuthRequestProcess;
import com.lingohammer.aws.auth.ResponseBuilder;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;

import javax.inject.Inject;

public class UserInfoProcess extends AuthRequestProcess<UserInfoRequest, UserInfoResponse> {


    @Inject
    public UserInfoProcess(ConfigurationService configurationService, UserService userService, LoggingService logger) {
        super(configurationService, userService, logger);
    }

    public void process(ResponseBuilder<UserInfoRequest, UserInfoResponse> response) {
        response.onInputAndPayload(this::refreshToken)//
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
