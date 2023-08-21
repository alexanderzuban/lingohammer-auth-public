package com.lingohammer.aws.auth.service.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingohammer.aws.auth.service.log.LoggingService;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

public class SsmParametersService {
    public static final String USER_POOL_PATH = "/api/%s/userPool/%s";
    private final LoggingService logger;
    private final ConfigurationService configurationService;
    private SsmClient ssmClient;
    private final String region;
    private final String stackName;

    private final Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build();

    public SsmParametersService(ConfigurationService configurationService, LoggingService logger) {
        this.logger = logger;
        this.region = configurationService.getRegion();
        this.stackName = configurationService.getStack();
        this.configurationService = configurationService;
    }

    public String getCognitoUserPoolId() {
        var result = getValue(String.format(USER_POOL_PATH, stackName, "userPoolId"));
        logger.log("Got userPoolId: " + result);
        return result;
    }

    public String getCognitoUserPoolClientId() {
        var result = getValue(String.format(USER_POOL_PATH, stackName, "userPoolClientId"));
        logger.log("Got userPoolClientId: " + result);
        return result;
    }

    public String getCognitoUserPoolClientSecret() {
        var result = getValue(String.format(USER_POOL_PATH, stackName, "userPoolClientSecret"));
        logger.log("Got userPoolClientSecret: " + result);
        return result;
    }

    private String getValue(String parameterName) {

        var result = cache.getIfPresent(parameterName);
        if (result == null) {
            result = getSsmDirect(parameterName);
            cache.put(parameterName, result);
        }
        return result;
    }

    private String getSsmDirect(String parameterName) {
        logger.log("Getting parameter from SSM: " + parameterName);

        return client().getParameter(getParameterRequest -> getParameterRequest
                        .withDecryption(true)
                        .name(parameterName)) //
                .parameter() //
                .value();
    }

    private SsmClient client() {
        return ofNullable(this.ssmClient).orElseGet(() -> {
            this.ssmClient = SsmClient.builder()
                    .credentialsProvider(configurationService.credentialsProvider())
                    .region(Region.of(configurationService.getRegion()))
                    .build();
            return this.ssmClient;
        });
    }
}
