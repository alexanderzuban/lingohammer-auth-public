package com.lingohammer.aws.auth.process.restore;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RestorePasswordConfirmHttpHandler extends HttpHandlerBase {

    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<RestorePasswordConfirmRequest, RestorePasswordConfirmResponse>(http, log(), RestorePasswordConfirmResponse::new);
        result.withBody(RestorePasswordConfirmRequest.class, getRequestBody(http));
        excuteProcess(RestorePasswordConfirmProcess.class, result).close();
    }
}
