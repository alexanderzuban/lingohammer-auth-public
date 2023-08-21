package com.lingohammer.aws.auth.process.register;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;


public class RegistrationConfirmLambdaHandler extends LambdaHandlerBase {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<RegistrationConfirmRequest, RegistrationConfirmResponse>(log(), RegistrationConfirmResponse::new);
        result.withBody(RegistrationConfirmRequest.class, request.getBody());
        return excuteProcess(RegistrationConfirmProcess.class, result);
    }

}
