package com.lingohammer.aws.auth.service;

import java.util.Optional;

public class ConfigurationService implements IConfigurationService {

    private SsmParametersService ssmService;

    public ConfigurationService() {
    }

    public ConfigurationService(SsmParametersService ssmService) {
        this.ssmService = ssmService;
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


    private SsmParametersService ssmService() {
        return Optional.ofNullable(this.ssmService)
                .orElseGet(() -> ssmService = new SsmParametersService(getRegion(), getStack()));
    }

}
