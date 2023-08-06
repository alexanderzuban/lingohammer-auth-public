package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.LoggingService;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class ResponseBuilder<K, T extends IsFlawed> extends IsFlawed {
    private final HashMap<String, String> headers;
    private final APIGatewayProxyResponseEvent response;
    private T payload;
    private K input;

    private LambdaLogger logger;

    public ResponseBuilder(Supplier<T> payloadSupplier) {
        payload = payloadSupplier.get();

        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response = new APIGatewayProxyResponseEvent();
    }

    public T getPayload() {
        return payload;
    }

    public void failed(AwsServiceException ex) {
        payload.fail(
                ex.awsErrorDetails().sdkHttpResponse().statusCode(), //
                ex.awsErrorDetails().errorMessage() //
        );
    }

    public void failed(Exception ex) {
        payload.fail(500, ex.getMessage());
    }

    public ResponseBuilder<K, T> withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public ResponseBuilder<K, T> withLogger(LambdaLogger logger) {
        this.logger = logger;
        LoggingService.LOG.withLoggable(new LoggingService.TargetLog() {
            @Override
            public void log(String message) {
                ofNullable(logger).ifPresent(l -> l.log(message));
            }

            @Override
            public void log(byte[] message) {
                ofNullable(logger).ifPresent(l -> l.log(message));
            }
        });
        return this;

    }

    public ResponseBuilder<K, T> withRequest(Class<K> inputClass, APIGatewayProxyRequestEvent request) {
     
        try {
            var mapper = ObjectMapperHelper.getObjectMapper();
            input = mapper.readValue(request.getBody(), inputClass);

            //TODO debug
            var requestContent = mapper.writeValueAsString(request);

            ofNullable(logger)
                    .ifPresent(l -> l.log("Input: " + requestContent));

            ofNullable(logger)
                    .ifPresent(l -> l.log("Request: " + request.getBody()));

        } catch (Exception ex) {
            ofNullable(logger)
                    .ifPresent(l -> l.log(ex.getMessage()));
            failed(ex);
        }
        return this;
    }


    public ResponseBuilder<K, T> onPayload(Consumer<T> action) {
        try {
            if (payload.isSuccess()) {
                action.accept(payload);
            }
        } catch (AwsServiceException ex) {
            ofNullable(logger)
                    .ifPresent(l -> l.log(ex.awsErrorDetails().errorMessage()));
            failed(ex);
        } catch (Exception ex) {
            ofNullable(logger)
                    .ifPresent(l -> l.log(ex.getMessage()));
            failed(ex);
        }

        return this;
    }

    public ResponseBuilder<K, T> onInputAndPayload(BiConsumer<K, T> action) {
        try {
            if (payload.isSuccess()) {
                action.accept(input, payload);
            }
        } catch (AwsServiceException ex) {
            ex.printStackTrace();
            ofNullable(logger)
                    .ifPresent(l -> l.log(ex.awsErrorDetails().errorMessage()));
            failed(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            ofNullable(logger)
                    .ifPresent(l -> l.log(ex.getMessage()));
            failed(ex);
        }

        return this;
    }

    public APIGatewayProxyResponseEvent done() {
        response.withHeaders(headers);
        response.withBody(ObjectMapperHelper.toJson(payload));
        response.withStatusCode(payload.getStatusCode());
        return response;
    }
}
