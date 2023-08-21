package com.lingohammer.aws.auth.service.configuration;

import com.lingohammer.aws.auth.service.log.LoggingService;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import javax.inject.Inject;
import java.util.Optional;


public class CloudConfigurationService implements ConfigurationService {

    private final LoggingService logger;

    private SsmParametersService ssmService;

    @Inject
    public CloudConfigurationService(LoggingService logger) {
        this.logger = logger;
    }

    @Override
    public String getRegion() {
        return System.getenv("AWS_REGION");
    }

    @Override
    public String getStack() {
        return System.getenv("STACK_NAME");
    }

    @Override
    public String getCognitoUserPoolId() {
        return ssmService().getCognitoUserPoolId();
    }

    @Override
    public String getCognitoUserPoolClientId() {
        return ssmService().getCognitoUserPoolClientId();
    }

    @Override
    public String getCognitoUserPoolClientSecret() {
        return ssmService().getCognitoUserPoolClientSecret();
    }

    public AwsCredentialsProvider credentialsProvider() {
        var profile = System.getenv("AWS_PROFILE");
        if (profile != null) {
            return ProfileCredentialsProvider.builder()
                    .profileName(profile)
                    .build();
        }
        return DefaultCredentialsProvider.create();
    }


    private SsmParametersService ssmService() {
        return Optional.ofNullable(this.ssmService)
                .orElseGet(() -> ssmService = new SsmParametersService(this, logger));
    }

}
