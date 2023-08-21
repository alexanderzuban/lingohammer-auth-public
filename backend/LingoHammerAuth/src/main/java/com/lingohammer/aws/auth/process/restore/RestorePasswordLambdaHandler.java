package com.lingohammer.aws.auth.process.restore;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;

public class RestorePasswordLambdaHandler extends LambdaHandlerBase {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<RestorePasswordRequest, RestorePasswordResponse>(log(), RestorePasswordResponse::new);
        result.withBody(RestorePasswordRequest.class, request.getBody());
        return excuteProcess(RestorePasswordProcess.class, result);
    }
}