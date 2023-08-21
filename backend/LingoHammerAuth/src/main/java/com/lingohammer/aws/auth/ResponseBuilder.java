package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.log.LoggingService;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class ResponseBuilder<I, O extends IsFlawed> extends IsFlawed {
    protected final HashMap<String, String> headers;

    protected final ResponseEvent response;
    protected O payload;
    protected I input;

    protected LoggingService logger;

    public ResponseBuilder(LoggingService logger, Supplier<O> payloadSupplier) {
        this.payload = payloadSupplier.get();
        this.logger = logger;

        this.headers = new HashMap<>();
        this.headers.put("Content-Type", "application/json");
        this.response = createResponseEvent();
    }

    protected ResponseEvent createResponseEvent() {
        return new ResponseEvent();
    }

    public O getPayload() {
        return payload;
    }


    public void failed(Exception ex) {
        ofNullable(logger)
                .ifPresent(l -> l.log(ex.getMessage()));
        payload.fail(500, ex.getMessage());
    }


    public ResponseBuilder<I, O> withBody(Class<I> inputClass, String body) {
        try {
            var mapper = ObjectMapperHelper.getObjectMapper();
            input = mapper.readValue(body, inputClass);
        } catch (Exception ex) {
            failed(ex);
        }
        return this;
    }


    public ResponseBuilder<I, O> onPayload(Consumer<O> action) {
        try {
            if (payload.isSuccess()) {
                action.accept(payload);
            }
        } catch (Exception ex) {
            failed(ex);
        }

        return this;
    }

    public ResponseBuilder<I, O> onInputAndPayload(BiConsumer<I, O> action) {
        try {
            if (payload.isSuccess()) {
                action.accept(input, payload);
            }
        } catch (Exception ex) {
            failed(ex);
        }

        return this;
    }

    public ResponseEvent done() {
        return response.withHeaders(headers)
                .withStatusCode(payload.getStatusCode())
                .withBody(ObjectMapperHelper.toJson(payload));

    }
}
