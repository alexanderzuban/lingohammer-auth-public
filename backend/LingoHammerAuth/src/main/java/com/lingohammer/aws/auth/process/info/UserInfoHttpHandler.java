package com.lingohammer.aws.auth.process.info;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class UserInfoHttpHandler extends HttpHandlerBase {

    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<UserInfoRequest, UserInfoResponse>(http, log(), UserInfoResponse::new);
        result.withBody(UserInfoRequest.class, getRequestBody(http));
        excuteProcess(UserInfoProcess.class, result).close();
    }
}
