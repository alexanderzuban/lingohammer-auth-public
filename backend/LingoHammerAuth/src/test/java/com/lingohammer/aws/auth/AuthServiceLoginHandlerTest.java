package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceLoginHandlerTest {
    private AuthServiceLoginHandler handler;

    @Mock
    private IUserService mockUserService;

    @Mock
    private IConfigurationService mockConfigurationService;

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

        var securityToken = new SecurityToken();
        var userInfo = new UserInfo();

        // Mock the IUserService
        when(mockUserService.checkUserExists("user@example.com", true)).thenReturn(true);
        when(mockUserService.login("user@example.com", "password")).thenReturn(securityToken);
        when(mockUserService.getUser(any())).thenReturn(userInfo);

        // Create the handler with mocked dependencies
        handler = new AuthServiceLoginHandler(mockConfigurationService, mockUserService);
    }


    private static Stream<Arguments> parametersForTestHandleRequest() {
        return Stream.of(
                //wrong password
                Arguments.of("{\"email\": \"user@example.com\", \"password\": \"wrongpassword\"}", 401),
                //missing user
                Arguments.of("{\"email\": \"nonexistent@example.com\", \"password\": \"password\"}", 401),
                Arguments.of("{\"email\": \"user@example.com\", \"password\": \"password\"}", 200)
        );
    }


    @ParameterizedTest
    @MethodSource("parametersForTestHandleRequest")
    void testHandleRequest(String body, int expectedStatusCode) {
        // Create a sample APIGatewayProxyRequestEvent with an invalid password
        var request = new APIGatewayProxyRequestEvent();
        request.setBody(body);

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(expectedStatusCode, response.getStatusCode());
    }
}
