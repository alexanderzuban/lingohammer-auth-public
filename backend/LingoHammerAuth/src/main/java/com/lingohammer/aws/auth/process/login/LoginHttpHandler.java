package com.lingohammer.aws.auth.process.login;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoginHttpHandler extends HttpHandlerBase {

    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<LoginRequest, LoginResponse>(http, log(), LoginResponse::new);
        result.withBody(LoginRequest.class, getRequestBody(http));
        excuteProcess(LoginProcess.class, result).close();
    }

}
