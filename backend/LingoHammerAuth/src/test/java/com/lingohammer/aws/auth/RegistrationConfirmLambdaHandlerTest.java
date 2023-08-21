package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;
import com.lingohammer.aws.auth.process.register.RegistrationConfirmLambdaHandler;
import com.lingohammer.aws.auth.service.configuration.CloudConfigurationService;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.service.user.UserService;
import org.codejargon.feather.Feather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RegistrationConfirmLambdaHandlerTest {

    private RegistrationConfirmLambdaHandler handler;

    @Mock
    private UserService mockUserService;

    @Mock
    private Context context;

    @Mock
    private LoggingService logger;

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
        when(mockUserService.confirmUserRegistration("user@example.com", "123456")).thenReturn(securityToken);
        when(mockUserService.login(any(), any())).thenReturn(securityToken);
        when(mockUserService.getUser(any())).thenReturn(userInfo);

        // Create the handler with mocked dependencies
        var configurationService = new CloudConfigurationService(logger);
        var di = Feather.with(new ServiceModuleMock(configurationService, mockUserService, logger));
        handler = new RegistrationConfirmLambdaHandler().withDependencies(di);
    }

    @Test
    void testHandleRequest() {
        // Create a sample APIGatewayProxyRequestEvent
        var request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"email\": \"user@example.com\", \"confirmationCode\": \"123456\"}");


        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(200, response.getStatusCode());
        // Add more assertions for the response if needed
    }
}
