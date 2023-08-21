package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.process.login.LoginHttpHandler;
import com.lingohammer.aws.auth.process.register.RegistrationConfirmHttpHandler;
import com.lingohammer.aws.auth.process.register.RegistrationHttpHandler;
import com.lingohammer.aws.auth.process.restore.RestorePasswordConfirmHttpHandler;
import com.lingohammer.aws.auth.process.restore.RestorePasswordHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LocalHttpService {

    public static void main(String[] args) {
        try {
            var server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/auth/user/login", new LoginHttpHandler());
            server.createContext("/auth/user/register", new RegistrationHttpHandler());
            server.createContext("/auth/user/confirm", new RegistrationConfirmHttpHandler());
            server.createContext("/auth/user/password/reset", new RestorePasswordHttpHandler());
            server.createContext("/auth/user/password/confirm", new RestorePasswordConfirmHttpHandler());
            // server.createContext("/auth/user", new UserInfoHttpHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            throw new AuthRuntimeException(e);
        }
    }
}