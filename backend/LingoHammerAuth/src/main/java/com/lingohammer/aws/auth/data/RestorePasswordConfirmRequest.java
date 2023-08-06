package com.lingohammer.aws.auth.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;

public class RestorePasswordConfirmRequest {

    @JsonDeserialize(using = ObjectMapperHelper.LowercaseDeserializer.class)
    private String email;

    private String newPassword;
    private String confirmationCode;

    public RestorePasswordConfirmRequest() {
    }

    public RestorePasswordConfirmRequest(String email, String newPassword, String confirmationCode) {
        this.email = email;
        this.newPassword = newPassword;
        this.confirmationCode = confirmationCode;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
