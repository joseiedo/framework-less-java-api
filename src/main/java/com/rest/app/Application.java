package com.rest.app;

import com.rest.app.config.Injector;
import com.rest.app.config.ServerManager;

import java.io.IOException;

public class Application {


    public static void main(String[] args) throws IOException {
        Injector.startApplication(Application.class);
        new ServerManager().start();


        //Example of basic auth
//        context.setAuthenticator(new BasicAuthenticator("myrealm") {
//            @Override
//            public boolean checkCredentials(String user, String pwd) {
//                return user.equals("admin") && pwd.equals("admin");
//            }
//        });
    }
}
