package com.rest.app;

import com.rest.app.config.Injector;
import com.rest.app.config.ServerManager;

import java.io.IOException;

public class Application {


    public static void main(String[] args) throws IOException {
        Injector.startApplication(Application.class);
        new ServerManager().start();

//        HttpContext context = server.createContext("/api/hello", (exchange -> {
//
//            if ("GET".equals(exchange.getRequestMethod())) {
//                Map<String, List<String>> params = ApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
//                String noNameText = "Anonymous";
//                String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
//                String respText = String.format("Hello %s!", name);
//                exchange.sendResponseHeaders(200, respText.getBytes().length);
//                OutputStream output = exchange.getResponseBody();
//                output.write(respText.getBytes());
//                output.flush();
//            } else {
//                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
//            }
//            exchange.close();
//        }));

//        context.setAuthenticator(new BasicAuthenticator("myrealm") {
//            @Override
//            public boolean checkCredentials(String user, String pwd) {
//                return user.equals("admin") && pwd.equals("admin");
//            }
//        });
//
//        server.setExecutor(null); // creates a default executor
//        server.start();
    }
}
