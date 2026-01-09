package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.ConsultationDAO;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.viewmodel.ConsultationSearchVM;
import hepl.dacsc.utils.QueryParser;
import hepl.dacsc.utils.SendResponse;

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

            SendResponse.sendResponse(exchange, 200, json);
        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    public void handlePut(HttpExchange exchange) {
        try {
            QueryParser queryParser = new QueryParser();
            Map<String, String> queryParams = queryParser.parseQuery(exchange.getRequestURI().getQuery());

            if (!queryParams.containsKey("id")) {
                SendResponse.sendResponse(exchange, 400, "Id de la consultation manquant");
                return;
            }

            Integer consultationId = Integer.parseInt(queryParams.get("id"));

            String body = new String(exchange.getRequestBody().readAllBytes());
            Map<String, String> bodyParams = queryParser.parseQuery(body);

            if (!bodyParams.containsKey("patientId") || !bodyParams.containsKey("reason")) {
                SendResponse.sendResponse(exchange, 400, "patientId ou reason manquant");
                return;
            }

            Integer patientId = Integer.parseInt(bodyParams.get("patientId"));
            String reason = bodyParams.get("reason");

            ConsultationDAO dao = new ConsultationDAO();

            dao.updateConsultation(consultationId, patientId, reason);

            String json = "{ \"message\": \"Consultation modifiée\", " + "\"consultationId\": " + consultationId + ", " + "\"patientId\": " + patientId + " }";
            SendResponse.sendResponse(exchange, 200, json);
        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }


    public void handleDelete(HttpExchange exchange) {
        try {
            QueryParser queryParser = new QueryParser();
            Map<String, String> params = queryParser.parseQuery(exchange.getRequestURI().getQuery());

            if (params.containsKey("id")) {
                Integer id = Integer.parseInt(params.get("id"));

                ConsultationDAO dao = new ConsultationDAO();

                boolean isDeleted = dao.deleteReservation(id);

                if (isDeleted) {
                    SendResponse.sendResponse(exchange, 200, "Reservation supprimees avec succes !");
                } else {
                    SendResponse.sendResponse(exchange, 200, "La reservation n'a pas ete supprimees");
                }
            }
            else SendResponse.sendResponse(exchange, 400, "L'id du patient est manquant !");
        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private String convertConsultationsToJson(List<Consultation> consultations, boolean reserved)
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
