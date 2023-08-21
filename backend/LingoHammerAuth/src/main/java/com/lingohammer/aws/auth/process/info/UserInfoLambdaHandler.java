package com.lingohammer.aws.auth.process.info;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.LambdaHandlerBase;
import com.lingohammer.aws.auth.LambdaResponseBuilder;

public class UserInfoLambdaHandler extends LambdaHandlerBase {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var result = new LambdaResponseBuilder<UserInfoRequest, UserInfoResponse>(log(), UserInfoResponse::new);
        result.withBody(UserInfoRequest.class, request.getBody());
        return excuteProcess(UserInfoProcess.class, result);
    }
}
