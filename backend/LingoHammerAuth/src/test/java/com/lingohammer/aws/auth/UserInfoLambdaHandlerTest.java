package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;
import com.lingohammer.aws.auth.process.info.UserInfoLambdaHandler;
import com.lingohammer.aws.auth.service.configuration.ConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;
import org.codejargon.feather.Feather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static com.lingohammer.aws.auth.util.StringAssertions.assertStringsEqualIgnoringLineEndings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserInfoLambdaHandlerTest {

    private UserInfoLambdaHandler handler;

    @Mock
    private UserService mockUserService;

    @Mock
    private ConfigurationService mockConfigurationService;

    @Mock
    private LoggingService mockLoggingService;

    @Mock
    private Context context;
    private AutoCloseable mockitoClosable;

    @AfterEach
    void closeService() throws Exception {
        mockitoClosable.close();
    }

    @BeforeEach
    public void setUp() {
        mockitoClosable = MockitoAnnotations.openMocks(this);

        var userInfo = new UserInfo();
        userInfo.setUserIdentifier("user123");
        userInfo.setEmail("user@example.com");

        var securityToken = new SecurityToken();
        securityToken.setSuccess(true);
        securityToken.setAccessToken("accessToken123");
        securityToken.setRefreshToken("refreshToken123");

        var failedToken = new SecurityToken();
        failedToken.setSuccess(false);

        var failedUserInfo = new UserInfo();
        failedUserInfo.setSuccess(false);

        // Mock the IUserService
        when(mockUserService.refreshToken("user123", "refreshToken123")).thenReturn(securityToken);
        when(mockUserService.getUser("accessToken123")).thenReturn(userInfo);

        when(mockUserService.refreshToken("invalidUser", "refreshToken123")).thenReturn(failedToken);
        when(mockUserService.refreshToken("user123", "invalidRefreshToken")).thenReturn(failedToken);
        when(mockUserService.getUser("invalidUser")).thenReturn(failedUserInfo);

        // Create the handler with mocked dependencies
        var di = Feather.with(new ServiceModuleMock(mockConfigurationService, mockUserService, mockLoggingService));
        handler = new UserInfoLambdaHandler().withDependencies(di);
    }

    public static Stream<Arguments> parametersForTestHandleRequest() {
        return Stream.of(
                //invalid user
                Arguments.of("{\"userIdentifier\": \"invalidUser\", \"refreshToken\": \"refreshToken123\"}", 400, "{  \"success\" : false,  \"statusCode\" : 400,  \"errorCode\" : -1}"),
                //invalid refresh token
                Arguments.of("{\"userIdentifier\": \"user123\", \"refreshToken\": \"invalidRefreshToken\"}", 400, "{  \"success\" : false,  \"statusCode\" : 400,  \"errorCode\" : -1}"),
                //valid user and refresh token
                Arguments.of("{\"userIdentifier\": \"user123\", \"refreshToken\": \"refreshToken123\"}", 200, "{  \"success\" : true,  \"statusCode\" : 200,  \"user\" : {    \"success\" : true,    \"statusCode\" : 200,    \"userIdentifier\" : \"user123\",    \"email\" : \"user@example.com\"  },  \"token\" : {    \"success\" : true,    \"statusCode\" : 200,    \"refreshToken\" : \"refreshToken123\",    \"accessToken\" : \"accessToken123\",    \"requiresConfirmation\" : false,    \"loggedIn\" : false  }}")
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForTestHandleRequest")
    void testHandleRequest(String body, int expectedStatusCode, String expectedBody) {
        // Create a sample APIGatewayProxyRequestEvent with an invalid user identifier
        var request = new APIGatewayProxyRequestEvent();
        request.setBody(body);

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(expectedStatusCode, response.getStatusCode());
        assertStringsEqualIgnoringLineEndings(expectedBody, response.getBody());
    }
}
