package com.lingohammer.aws.auth.process.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;

public class LoginRequest {

    @JsonDeserialize(using = ObjectMapperHelper.LowercaseDeserializer.class)
    private String email;
    private String password;

    public LoginRequest() {

    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
