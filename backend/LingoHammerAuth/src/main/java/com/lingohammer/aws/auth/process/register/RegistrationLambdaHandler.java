package com.lingohammer.aws.auth.process.register;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;

public class RegistrationLambdaHandler extends LambdaHandlerBase {
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<RegistrationRequest, RegistrationResponse>(log(), RegistrationResponse::new);
        result.withBody(RegistrationRequest.class, request.getBody());
        return excuteProcess(RegistrationProcess.class, result);
    }
}
