package com.lingohammer.aws.auth;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.service.log.LoggingService;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class LambdaResponseBuilder<K, T extends IsFlawed> extends ResponseBuilder<K, T> {

    final APIGatewayProxyResponseEvent gatewayResponseEvent;

    public LambdaResponseBuilder(LoggingService logger, Supplier<T> payloadSupplier) {
        super(logger, payloadSupplier);

        gatewayResponseEvent = new APIGatewayProxyResponseEvent();
    }

    public void failed(AwsServiceException ex) {
        ofNullable(logger)
                .ifPresent(l -> l.log(ex.awsErrorDetails().errorMessage()));

        payload.fail(
                ex.awsErrorDetails().sdkHttpResponse().statusCode(), //
                ex.awsErrorDetails().errorMessage() //
        );
    }

    @Override
    public void failed(Exception ex) {
        if (ex instanceof AwsServiceException awse) {
            failed(awse);
            return;
        }
        super.failed(ex);
    }


    @Override
    protected ResponseEvent createResponseEvent() {
        return new ResponseEvent() {

            @Override
            public ResponseEvent withBody(String body) {
                gatewayResponseEvent.withBody(body);
                return this;
            }

            @Override
            public ResponseEvent withStatusCode(int statusCode) {
                gatewayResponseEvent.withStatusCode(statusCode);
                return this;
            }

            @Override
            public ResponseEvent withHeaders(Map<String, String> headers) {
                gatewayResponseEvent.withHeaders(headers);
                return this;
            }

        };
    }

    public APIGatewayProxyResponseEvent getGatewayResponseEvent() {
        return gatewayResponseEvent;
    }


}
