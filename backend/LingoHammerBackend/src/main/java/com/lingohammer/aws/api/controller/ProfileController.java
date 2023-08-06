package com.lingohammer.aws.api.controller;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.services.lambda.runtime.Context;
import com.lingohammer.aws.api.data.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/")
public class ProfileController {

    @GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUser(HttpServletRequest request) {

        var result = new ArrayList<>(
                List.of(
                        new User("John 1", "Doe 1", "john.doe@baeldung.com"),
                        new User("John 2", "Doe 2", "john.doe-2@baeldung.com")
                ));


        var contextAttribute = request.getAttribute(RequestReader.LAMBDA_CONTEXT_PROPERTY);
        if (contextAttribute instanceof Context lambdaContext) {
            // Access the properties or methods of the lambdaContext object as needed
            var awsRequestId = lambdaContext.getAwsRequestId();
            var functionName = lambdaContext.getFunctionName();
            var user = new User(
                    lambdaContext.getFunctionName(),
                    lambdaContext.getAwsRequestId(),
                    lambdaContext.getInvokedFunctionArn());
            result.add(0, user);
        }
        return result;
    }
}
