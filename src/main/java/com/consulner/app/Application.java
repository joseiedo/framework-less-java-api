package com.consulner.app;

import com.consulner.app.api.ApiUtils;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/hello", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = ApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                String noNameTxt = "Anonymous";
                String name = params.getOrDefault("name", List.of(noNameTxt)).stream().findFirst().orElse(noNameTxt);
                String respText = String.format("Hello %s!", name);

                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.setExecutor(null);
        server.start();
    }
}