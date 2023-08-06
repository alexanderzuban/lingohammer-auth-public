package com.lingohammer.aws.auth.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;

public class RestorePasswordRequest {
    @JsonDeserialize(using = ObjectMapperHelper.LowercaseDeserializer.class)
    private String email;

    public RestorePasswordRequest(String email) {
        this.email = email;
    }

    public RestorePasswordRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
