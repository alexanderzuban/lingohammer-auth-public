package com.lingohammer.aws.auth.service.configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public interface ConfigurationService {
    String getRegion();

    String getStack();

    String getCognitoUserPoolId();

    String getCognitoUserPoolClientId();

    String getCognitoUserPoolClientSecret();

    AwsCredentialsProvider credentialsProvider();
}
