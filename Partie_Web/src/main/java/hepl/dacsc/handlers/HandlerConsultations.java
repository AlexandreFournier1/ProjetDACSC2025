package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.ConsultationDAO;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.viewmodel.ConsultationSearchVM;
import hepl.dacsc.utils.QueryParser;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        try {
            QueryParser queryParser = new QueryParser();
            Map<String, String> params = queryParser.parseQuery(exchange.getRequestURI().getQuery());

            ConsultationSearchVM searchVM = new ConsultationSearchVM();
            boolean reserved = false;

            if (params.containsKey("date")) {
                searchVM.setDate(LocalDate.parse(params.get("date")));
            }

            if (params.containsKey("doctor")) {
                searchVM.setDoctor_name(params.get("doctor"));
            }

            if (params.containsKey("specialty")) {
                searchVM.setSpeciality_name(params.get("specialty"));
            }

            if (params.containsKey("patientId")) {
                searchVM.setPatient_id(Integer.parseInt(params.get("patientId")));
                reserved = true;
            }

            ConsultationDAO dao = new ConsultationDAO();

            List<Consultation> consultations = dao.loadConsultations(searchVM);

            String json = convertConsultationsToJson(consultations, reserved);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, json.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handlePut(HttpExchange exchange) {

    }

    public void handleDelete(HttpExchange exchange) {

    }

    private static String convertConsultationsToJson(List<Consultation> consultations, boolean reserved)
    {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < consultations.size(); i++)
        {
            Consultation c = consultations.get(i);

            json.append("{");
            json.append("\"id\": ").append(c.getId()).append(",");
            json.append("\"reserved\": ").append(reserved).append(",");
            json.append("\"doctor_id\": ").append(c.getDoctor_id()).append(",");
            json.append("\"doctor_name\": \"").append(c.getDoctor_name()).append("\",");
            json.append("\"specialty\": ").append(c.getSpeciality_name()).append(",");
            if (reserved) {
                json.append("\"patient_id\": ").append(c.getPatient_id()).append(",");
            }
            json.append("\"date\": ").append(LocalDate.parse(c.getDate().toString())).append(",");
            json.append("\"hour\": ").append(LocalTime.parse(c.getHour().toString())).append(",");
            json.append("}");

            if (i < consultations.size() - 1)
                json.append(",");
        }

        json.append("]");
        return json.toString();
    }
}
