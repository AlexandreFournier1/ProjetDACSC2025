package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.DoctorDAO;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;
import hepl.dacsc.utils.QueryParser;
import hepl.dacsc.utils.SendResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class HandlerDoctors implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        System.out.println("Request Path = " + requestPath);
        System.out.println("Request Method = " + method);

        if (!method.equalsIgnoreCase("GET")) {
            SendResponse.sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        handleGet(exchange);
    }

    public void handleGet(HttpExchange exchange) {
        try {
            QueryParser queryParser = new QueryParser();
            Map<String, String> params = queryParser.parseQuery(exchange.getRequestURI().getQuery());

            DoctorSearchVM vm = new DoctorSearchVM();

            if (params.containsKey("name")) {
                vm.setLast_name(params.get("name"));
            }

            if (params.containsKey("specialty")) {
                vm.setSpecialty_name(params.get("specialty"));
            }

            DoctorDAO dao = new DoctorDAO();

            List<Doctor> doctors = dao.loadDoctor(vm);

            String json = convertDoctorsToJson(doctors);

            SendResponse.sendResponse(exchange, 200, json);
        } catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private static String convertDoctorsToJson(List<Doctor> doctors)
    {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < doctors.size(); i++)
        {
            Doctor d = doctors.get(i);

            json.append("{");
            json.append("\"id\": ").append(d.getId()).append(",");
            json.append("\"lastName\": \"").append(d.getLast_name()).append("\",");
            json.append("\"firstName\": \"").append(d.getFirst_name()).append("\",");
            json.append("\"specialty\": \"").append(d.getSpeciality_name()).append("\"");
            json.append("}");

            if (i < doctors.size() - 1)
                json.append(",");
        }

        json.append("]");
        return json.toString();
    }
}
