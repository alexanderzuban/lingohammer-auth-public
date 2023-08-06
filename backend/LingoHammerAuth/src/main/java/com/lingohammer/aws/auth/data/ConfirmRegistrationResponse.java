package com.lingohammer.aws.auth.data;

public class ConfirmRegistrationResponse extends IsFlawed {
    public static final int ERROR_CODE_MISSING_USER = -1;
    public static final int ERROR_CODE_WRONG_CONFIRMATION_CODE = -2;

    private SecurityToken token;

    private UserInfo user;

    public SecurityToken getToken() {
        return token;
    }

    public void setToken(SecurityToken token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
