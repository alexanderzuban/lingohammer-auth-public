package com.lingohammer.aws.auth.process.login;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;

public class LoginResponse extends IsFlawed {
    public static final int ERROR_CODE_MISSING_USER = -1;
    public static final int ERROR_CODE_WRONG_PASSWORD = -2;

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
