package com.lingohammer.aws.auth.process.restore;

import com.lingohammer.aws.auth.HttpHandlerBase;
import com.lingohammer.aws.auth.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RestorePasswordHttpHandler extends HttpHandlerBase {

    @Override
    public void handle(HttpExchange http) throws IOException {
        var result = new HttpResponseBuilder<RestorePasswordRequest, RestorePasswordResponse>(http, log(), RestorePasswordResponse::new);
        result.withBody(RestorePasswordRequest.class, getRequestBody(http));
        excuteProcess(RestorePasswordProcess.class, result).close();
    }
}
