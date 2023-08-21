package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.service.ServicesModule;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;
import org.codejargon.feather.Feather;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ServicesModuleTest {

    public static Stream<Arguments> parametersForTestDependencyInjection() {
        return Stream.of(
                Arguments.of(ConfigurationService.class),
                Arguments.of(UserService.class),
                Arguments.of(LoggingService.class)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForTestDependencyInjection")
    void testDependencyInjection(Class<?> serviceClass) {
        var di = Feather.with(new ServicesModule());
        var service = di.instance(serviceClass);
        assert service != null;
    }
}
