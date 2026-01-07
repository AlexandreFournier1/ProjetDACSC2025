package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HandlerPatients implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        System.out.println("Request Path = " + requestPath);
        System.out.println("Request Method = " + method);

        if (method.equalsIgnoreCase("POST")) {
            handlePost(exchange);
        }
    }

    public void handlePost(HttpExchange exchange) {

    }
}
