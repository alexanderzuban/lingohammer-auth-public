package com.lingohammer.aws.auth.process.register;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RegistrationHttpHandler extends HttpHandlerBase {
    
    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<RegistrationRequest, RegistrationResponse>(http, log(), RegistrationResponse::new);
        result.withBody(RegistrationRequest.class, getRequestBody(http));
        excuteProcess(RegistrationProcess.class, result).close();
    }
}
