package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.service.IConfigurationService;
import com.lingohammer.aws.auth.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static com.lingohammer.aws.auth.util.StringAssertions.assertStringsEqualIgnoringLineEndings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthServiceRegisterHandlerTest {
    private AuthServiceRegisterHandler handler;

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

        // Create the handler with the mocked IUserService
        handler = new AuthServiceRegisterHandler(mockConfigurationService, mockUserService);
    }

    @Test
    void testHandleRequestUserExists() {
        // Create a sample APIGatewayProxyRequestEvent with an existing user email
        var request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"email\": \"user@example.com\", \"password\": \"password\", \"firstName\": \"John\", \"lastName\": \"Doe\"}");

        // Mock the UserService to return true for user existence check
        when(mockUserService.checkUserExists("user@example.com", true)).thenReturn(true);

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(400, response.getStatusCode());
        assertStringsEqualIgnoringLineEndings("""
                {  "success" : false,  "statusCode" : 400,  "errorCode" : -1,  "errorMessage" : "User already exists"}
                 """, response.getBody());
    }

    @Test
    void testHandleRequestRegisterUserSuccess() {
        // Create a sample APIGatewayProxyRequestEvent with a new user email
        var request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"email\": \"newuser@example.com\", \"password\": \"password\", \"firstName\": \"John\", \"lastName\": \"Doe\"}");

        // Mock the UserService to return false for user existence check
        when(mockUserService.checkUserExists("newuser@example.com", true)).thenReturn(false);

        // Mock the UserService to return a success result for user registration
        var successResult = new SecurityToken();
        successResult.setSuccess(true);
        when(mockUserService.registerUser("newuser@example.com", "password", Map.of("firstName", "John", "lastName", "Doe")))
                .thenReturn(successResult);

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(200, response.getStatusCode());
        // Add more assertions for the response if needed
    }

    @Test
    void testHandleRequestRegisterUserFailure() {
        // Create a sample APIGatewayProxyRequestEvent with a new user email
        var request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"email\": \"newuser@example.com\", \"password\": \"password\", \"firstName\": \"John\", \"lastName\": \"Doe\"}");

        // Mock the UserService to return false for user existence check
        when(mockUserService.checkUserExists("newuser@example.com", true)).thenReturn(false);

        // Mock the UserService to return an error result for user registration
        var errorResult = new SecurityToken();
        errorResult.setSuccess(false);
        errorResult.setErrorMessage("User registration failed");
        when(mockUserService.registerUser("newuser@example.com", "password", Map.of("firstName", "John", "lastName", "Doe")))
                .thenReturn(errorResult);

        // Call the handleRequest method
        var response = handler.handleRequest(request, context);

        // Verify the response
        assertEquals(400, response.getStatusCode());
        assertStringsEqualIgnoringLineEndings("""
                {  "success" : false,  "statusCode" : 400,  "errorMessage" : "User registration failed"}
                """, response.getBody());
    }
}
