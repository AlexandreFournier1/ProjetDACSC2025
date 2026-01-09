package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.PatientDAO;
import hepl.dacsc.model.entity.Patient;
import hepl.dacsc.model.viewmodel.PatientSearchVM;
import hepl.dacsc.utils.QueryParser;
import hepl.dacsc.utils.SendResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

            if (!params.containsKey("last_name") || !params.containsKey("first_name") || !params.containsKey("newPatient")) {
                SendResponse.sendResponse(exchange, 400, "Last_name, First_name, newPatient non fourni");
                return;
            }

            Integer idPatient = null;
            String lastName = params.get("last_name");
            String firstName = params.get("first_name");
            boolean isNewPatient = Boolean.parseBoolean(params.get("newPatient"));
            if(!isNewPatient) {
                if(!params.containsKey("idPatient")){
                    SendResponse.sendResponse(exchange, 400, "idPatient non fourni");
                    return;
                }
                idPatient = Integer.parseInt(params.get("idPatient"));
            }
            LocalDate birthDate = null;
            if (params.containsKey("birth_date")) {
                birthDate = LocalDate.parse(params.get("birth_date"));
            }

            PatientDAO dao = new PatientDAO();

            if(isNewPatient) {
                Patient patient = new Patient(null, lastName, firstName, birthDate);
                dao.save(patient);
                String json = "{ \"message\": \"Patient créé\", \"id\": " + patient.getId() + " }";
                SendResponse.sendResponse(exchange, 200, json);
            } else {
                PatientSearchVM search = new PatientSearchVM();
                search.setFirst_name(firstName);
                search.setLast_name(lastName);
                List<Patient> existing = dao.loadPatients(search);

                if(!existing.isEmpty()) {
                    String json = "{ \"message\": \"Patient existant\", \"id\": " + existing.get(0).getId() + " }";
                    SendResponse.sendResponse(exchange, 200, json);
                } else {
                    SendResponse.sendResponse(exchange, 400, "Patient Inexistant");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }


}
