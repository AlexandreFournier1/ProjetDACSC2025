package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.PatientDAO;
import hepl.dacsc.model.entity.Patient;
import hepl.dacsc.utils.QueryParser;
import hepl.dacsc.utils.SendResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

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
        try {
            String body = new String(exchange.getRequestBody().readAllBytes());

            Map<String, String> params = new QueryParser().parseQuery(body);

            if (!params.containsKey("last_name") || !params.containsKey("first_name")) {
                SendResponse.sendResponse(exchange, 400, "Last_name ou First_name non fourni");
                return;
            }

            String lastName = params.get("last_name");
            String firstName = params.get("first_name");

            LocalDate birthDate = null;
            if (params.containsKey("birth_date")) {
                birthDate = LocalDate.parse(params.get("birth_date"));
            }

            Patient patient = new Patient(null, lastName, firstName, birthDate);

            PatientDAO dao = new PatientDAO();
            dao.save(patient);

            String json = "{ \"message\": \"Patient créé\", \"id\": " + patient.getId() + " }";
            SendResponse.sendResponse(exchange, 201, json);
        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }


}
