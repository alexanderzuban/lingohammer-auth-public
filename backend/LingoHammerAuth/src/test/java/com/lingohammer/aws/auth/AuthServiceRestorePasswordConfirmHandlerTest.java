package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceRestorePasswordConfirmHandlerTest {
    private AuthServiceRestorePasswordConfirmHandler handler;

    @Mock
    private IUserService mockUserService;

    @Mock
    private IConfigurationService mockConfigurationServic;

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

        // Mock the IUserService
        when(mockUserService.login(any(), any())).thenReturn(new SecurityToken());
        when(mockUserService.getUser(any())).thenReturn(new UserInfo());
        when(mockUserService.confirmRestorePassword(any(), any(), any())).thenReturn(new IsFlawed());

        // Create the handler with mocked dependencies
        handler = new AuthServiceRestorePasswordConfirmHandler(mockConfigurationServic, mockUserService);
    }

    @Test
    void testHandleRequest() {
        // Create a sample APIGatewayProxyRequestEvent
        var request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"email\": \"user@example.com\", \"confirmationCode\": \"123456\", \"newPassword\": \"newPassword\"}");

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(200, response.getStatusCode());

    }
}