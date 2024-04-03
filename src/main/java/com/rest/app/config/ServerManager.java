package com.rest.app.config;

import com.rest.app.utils.Path;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

public class ServerManager {

    private final HttpServer server;

    private final ClassHunterWrapper classHunterWrapper;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerManager() throws IOException {
        int serverPort = 8080;
        this.classHunterWrapper = new ClassHunterWrapper();
        this.server = HttpServer.create(new InetSocketAddress(serverPort), 0);
    }

    public void start() {
        Collection<Class<?>> classesWithEndpoints = findClassesWithEndpoints();
        classesWithEndpoints.forEach(this::setupEndpointsInClassMethods);

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void setupEndpointsInClassMethods(Class<?> cls) {
        Path pathInClass = cls.getAnnotation(Path.class);

        if (pathInClass == null) {
            logger.severe("Class " + cls.getName() + " does not have a Path annotation");
            return;
        }

        Arrays.stream(cls.getMethods())
                .filter(method -> {
                            try {
                                return method.getAnnotation(Path.class).value() != null;
                            } catch (NullPointerException e) {
                                return false;
                            }
                        }
                ).forEach(method -> {
                    Path pathInMethod = method.getAnnotation(Path.class);
                    String endpoint = pathInClass.value() + pathInMethod.value();
                    setupServerContext(endpoint, method, cls);
                });
    }


    public void setupServerContext(String endpoint, Method method, Class<?> cls) {
        server.createContext(endpoint, (exchange -> {
            try {
                Object controller = Injector.getService(cls);
                method.invoke(controller, exchange);
            } catch (Exception e) {
                logger.severe("Error while creating endpoint for path " + endpoint + " method " + method.getName() + " in class " + cls.getName());
            }
        }));
    }


    private Collection<Class<?>> findClassesWithEndpoints() {
        return classHunterWrapper.findAllThatMatch(cls -> cls.isAnnotationPresent(Path.class));
    }
}
