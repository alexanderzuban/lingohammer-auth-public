package com.lingohammer.aws.auth.process.register;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;

public class RegistrationConfirmRequest {
    private String confirmationCode;

    @JsonDeserialize(using = ObjectMapperHelper.LowercaseDeserializer.class)
    private String email;


    public RegistrationConfirmRequest() {
    }

    public RegistrationConfirmRequest(String email, String password, String confirmationCode) {
        this.confirmationCode = confirmationCode;
        this.email = email;
        this.password = password;
    }

    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
