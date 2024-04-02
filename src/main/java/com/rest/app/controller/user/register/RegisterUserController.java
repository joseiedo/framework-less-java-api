package com.rest.app.controller.user.register;

import com.rest.app.config.exceptions.ApplicationExceptions;
import com.rest.app.controller.Controller;
import com.rest.app.domain.user.NewUser;
import com.rest.app.domain.user.UserService;
import com.rest.app.utils.*;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@Component
@Path("/api/users/register")
public class RegisterUserController extends Controller {

    @Inject
    public UserService userService;


    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity<RegistrationResponse> e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getHttpStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<RegistrationResponse> doPost(InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);

        NewUser user = NewUser.builder()
                .login(registerRequest.getLogin())
                .password(PasswordEncoder.encode(registerRequest.getPassword()))
                .build();

        String userId = userService.create(user);

        RegistrationResponse response = new RegistrationResponse(userId);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), HttpStatusCode.OK);
    }
}
