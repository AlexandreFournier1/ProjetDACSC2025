package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HandlerConsultations implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        System.out.println("Request Path = " + requestPath);
        System.out.println("Request Method = " + method);

        if (method.equalsIgnoreCase("GET")) {
            handleGet(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            handlePut(exchange);
        } else if (method.equalsIgnoreCase("DELETE")) {
            handleDelete(exchange);
        }
    }

    public void handleGet(HttpExchange exchange) {

    }

    public void handlePut(HttpExchange exchange) {

    }

    public void handleDelete(HttpExchange exchange) {

    }
}
