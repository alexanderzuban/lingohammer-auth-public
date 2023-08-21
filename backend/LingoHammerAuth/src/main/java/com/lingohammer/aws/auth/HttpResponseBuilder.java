package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.function.Supplier;

public class HttpResponseBuilder<K, T extends IsFlawed> extends ResponseBuilder<K, T> {
    private final HttpExchange http;

    public HttpResponseBuilder(HttpExchange http, LoggingService logger, Supplier<T> payloadSupplier) {
        super(logger, payloadSupplier);
        this.http = http;
    }

    @Override
    protected ResponseEvent createResponseEvent() {
        return new ResponseEvent() {
            @Override
            public ResponseEvent withBody(String body) {
                try {
                    http.getResponseBody().write(body.getBytes());
                } catch (IOException e) {
                    throw new AuthRuntimeException(e);
                }
                return this;
            }

            @Override
            public ResponseEvent withStatusCode(int statusCode) {
                try {
                    http.sendResponseHeaders(statusCode, 0);
                } catch (IOException e) {
                    throw new AuthRuntimeException(e);
                }
                return this;
            }

            @Override
            public ResponseEvent withHeaders(java.util.Map<String, String> headers) {
                headers.forEach((k, v) -> http.getResponseHeaders().add(k, v));
                return this;
            }
        };
    }

    public HttpExchange getHttp() {
        return http;
    }
}
