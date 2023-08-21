package com.lingohammer.aws.auth.process.restore;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;

public class RestorePasswordConfirmLambdaHandler extends LambdaHandlerBase {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<RestorePasswordConfirmRequest, RestorePasswordConfirmResponse>(log(), RestorePasswordConfirmResponse::new);
        result.withBody(RestorePasswordConfirmRequest.class, request.getBody());
        return excuteProcess(RestorePasswordConfirmProcess.class, result);
    }
}
