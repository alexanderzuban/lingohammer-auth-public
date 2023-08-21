package com.lingohammer.aws.auth.process.info;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;

public class UserInfoResponse extends IsFlawed {
    public static final int ERROR_CODE_INVALID_REFRESH_TOKEN = -1;
    public static final int ERROR_CODE_INVALID_USER = -2;
    private UserInfo user;

    private SecurityToken token;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public SecurityToken getToken() {
        return token;
    }

    public void setToken(SecurityToken token) {
        this.token = token;
    }
}
