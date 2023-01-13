package webservice;

import com.google.gson.Gson;
import datamart.DatabaseReader;

import java.time.LocalDate;

import static spark.Spark.*;
import static spark.Spark.notFound;

public class SparkWebService implements WebService {
    private final Gson gson;
    private final DatabaseReader database;

    public SparkWebService(DatabaseReader database) {
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
                if (database.readMaxTemperatureBetween(from, to).isEmpty()) halt(404, "No se dispone de datos para estas fechas, o el formato es incorrecto [YYYY-mm-DD].");
                return gson.toJson(database.readMaxTemperatureBetween(from, to));
            }));

            get("/with-min-temperature", "application/json", ((request, response) -> {
                response.type("application/json");
                String from = paramExist(request.queryParams("from"));
                String to = paramExist(request.queryParams("to"));
                if (database.readMinTemperatureBetween(from, to).isEmpty()) halt(404, "No se dispone de datos para estas fechas, o el formato es incorrecto [YYYY-mm-DD]. \s");
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
}
