package com.lingohammer.aws.auth.process.login;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;

public class LoginLambdaHandler extends LambdaHandlerBase {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<LoginRequest, LoginResponse>(log(), LoginResponse::new);
        result.withBody(LoginRequest.class, request.getBody());
        return excuteProcess(LoginProcess.class, result);
    }
}
