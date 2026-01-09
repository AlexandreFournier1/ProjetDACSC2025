package hepl.dacsc.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import hepl.dacsc.model.dao.SpecialityDAO;
import hepl.dacsc.model.entity.Speciality;
import hepl.dacsc.model.viewmodel.SpecialitySearchVM;
import hepl.dacsc.utils.QueryParser;
import hepl.dacsc.utils.SendResponse;

import java.util.List;
import java.util.Map;
public class HandlerSpecialities implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        System.out.println("Request Path = " + requestPath);
        System.out.println("Request Method = " + method);

        if (method.equalsIgnoreCase("GET")) {
            handleGet(exchange);
        }
    }

    public void handleGet(HttpExchange exchange) {
        try{
            SpecialityDAO dao = new SpecialityDAO();
            List<Speciality> speciality = dao.loadSpeciality();
            String json = convertSpecialityToJson(speciality);
            SendResponse.sendResponse(exchange, 200, json);

        }catch (Exception e) {
            e.printStackTrace();
            SendResponse.sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private String convertSpecialityToJson(List<Speciality> speciality) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < speciality.size(); i++)
        {
            Speciality c = speciality.get(i);

            json.append("{");
            json.append("\"id\": ").append(c.getId()).append(",");
            json.append("\"name\": ").append(c.getName()).append(",");
            json.append("}");

            if (i < speciality.size() - 1)
                json.append(",");
        }

        json.append("]");
        return json.toString();
    }

}
