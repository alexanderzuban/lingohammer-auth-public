package com.lingohammer.aws.auth;

import java.util.Map;

public class ResponseEvent {
    private String body;
    private Map<String, String> headers;

    private int statusCode;

    public ResponseEvent withBody(String body) {
        this.body = body;
        return this;
    }

    public ResponseEvent withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ResponseEvent withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}
