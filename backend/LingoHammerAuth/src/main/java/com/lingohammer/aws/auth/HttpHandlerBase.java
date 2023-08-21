package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.ServicesModule;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.codejargon.feather.Feather;

import java.io.IOException;

public abstract class HttpHandlerBase implements HttpHandler {

    private Feather di;

    protected HttpHandlerBase() {
    }

    public Feather DI() {
        if (di == null) {
            installDI();
        }
        return di;
    }

    private synchronized void installDI() {
        if (di == null) {
            di = Feather.with(new ServicesModule());
        }
    }

    public <T> T withDependencies(Feather di) {
        this.di = di;
        return (T) this;
    }

    protected String getRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    protected <I, O extends IsFlawed, P extends AuthRequestProcess<I, O>> HttpExchange excuteProcess(Class<P> processClass, HttpResponseBuilder<I, O> result) {
        var process = DI().instance(processClass);
        process.process(result);
        return result.getHttp();
    }

    protected LoggingService log() {
        return DI().instance(LoggingService.class);
    }
}
