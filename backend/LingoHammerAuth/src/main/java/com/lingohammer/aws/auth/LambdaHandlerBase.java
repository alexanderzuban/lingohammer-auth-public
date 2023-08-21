package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.ServicesModule;
import com.lingohammer.aws.auth.service.log.LoggingService;
import org.codejargon.feather.Feather;

public abstract class LambdaHandlerBase implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected Feather di;

    protected LambdaHandlerBase() {
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


    protected <I, O extends IsFlawed, P extends AuthRequestProcess<I, O>> APIGatewayProxyResponseEvent excuteProcess(Class<P> processClass, LambdaResponseBuilder<I, O> result) {
        var process = DI().instance(processClass);
        process.process(result);
        return result.getGatewayResponseEvent();
    }

    protected LoggingService log() {
        return DI().instance(LoggingService.class);
    }
}
