package com.lingohammer.aws.auth.service;

public interface IConfigurationService {
    String getRegion();

    String getStack();

    String getCognitoUserPoolId();

    String getCognitoUserPoolClientId();

    String getCognitoUserPoolClientSecret();
}
