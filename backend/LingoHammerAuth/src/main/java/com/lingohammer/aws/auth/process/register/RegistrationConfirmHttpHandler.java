package com.lingohammer.aws.auth.process.register;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RegistrationConfirmHttpHandler extends HttpHandlerBase {

    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<RegistrationConfirmRequest, RegistrationConfirmResponse>(http, log(), RegistrationConfirmResponse::new);
        result.withBody(RegistrationConfirmRequest.class, getRequestBody(http));
        excuteProcess(RegistrationConfirmProcess.class, result).close();
    }
}
