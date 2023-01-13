package webservice;

import com.google.gson.Gson;
import datamart.DatamartReader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static spark.Spark.*;
import static spark.Spark.notFound;

public class SparkWebService implements WebService {
    private final Gson gson;
    private final DatamartReader database;

    public SparkWebService(DatamartReader database) {
        this.database = database;
        gson = new Gson();
    }

    @Override
    public void run() {
        path("/v1/places", () -> {
            get("/with-max-temperature", "application/json", ((request, response) -> {
                response.type("application/json");
                String from = paramExist(request.queryParams("from"));
                String to = paramExist(request.queryParams("to"));
                if (!isValidDate(from) || !isValidDate(to)) halt(400, "El formato es incorrecto. Formato correcto: YYYY-mm-DD.");
                if (database.readMaxTemperatureBetween(from, to).isEmpty()) halt(404, "No se dispone de datos para estas fechas.");
                return gson.toJson(database.readMaxTemperatureBetween(from, to));
            }));

            get("/with-min-temperature", "application/json", ((request, response) -> {
                response.type("application/json");
                String from = paramExist(request.queryParams("from"));
                String to = paramExist(request.queryParams("to"));
                if (!isValidDate(from) || !isValidDate(to)) halt(400, "El formato es incorrecto. Formato correcto: YYYY-mm-DD.");
                if (database.readMinTemperatureBetween(from, to).isEmpty()) halt(404, "No se dispone de datos para estas fechas.");
                return gson.toJson(database.readMinTemperatureBetween(from, to));
            }));

            notFound("""
                    Página desconocida, prueba con uno de los siguientes formatos:\s
                    /v1/places/with-min-temperature\s
                    /v1/places/with-max-temperature
                    """);
        });
    }

    private String paramExist(String param) {
        if (param == null || param.equals("")) return LocalDate.now().toString();
        else {
            return param;
        }
    }

    private boolean isValidDate(String value) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
