package sensor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datalake.POJO.Weather;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AemetWeatherSensor implements WeatherSensor {

    private final String apikey;
    private final Area area;
    private final String URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";
    private final Gson gson;

    public AemetWeatherSensor(String apikey, Area area) {
        this.apikey = apikey;
        this.area = area;
        gson = new Gson();
    }

    public List<Weather> read() throws IOException {
        String response = request(URL);
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String dataUrl = jsonObject.getAsJsonPrimitive("datos").getAsString();
        System.out.println("content1: " + LocalDateTime.now());
        String content = request(dataUrl);
        System.out.println("content2: " + LocalDateTime.now());
        JsonArray jsonElements = gson.fromJson(content, JsonArray.class);
        List<JsonElement> filteredData = filterAemetStationsData(jsonElements);
        return convertToWeather(filteredData);
    }

    private List<JsonElement> filterAemetStationsData(JsonArray jsonElements) {
        return jsonElements.asList().stream()
                .filter(data -> area.fromLat() < data.getAsJsonObject().get("lat").getAsDouble() && data.getAsJsonObject().get("lat").getAsDouble() < area.toLat())
                .filter(data -> area.fromLon() < data.getAsJsonObject().get("lon").getAsDouble() && data.getAsJsonObject().get("lon").getAsDouble() < area.toLon())
                .toList();
    }

    private List<Weather> convertToWeather(List<JsonElement> jsonElements) {
        List<Weather> filteredData = new ArrayList<>();
        jsonElements.forEach(jsonElement -> {
            if (hasTemperature(jsonElement)) filteredData.add(getWeather(jsonElement));
        });
        return filteredData;
    }

    private boolean hasTemperature(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("tamax") != null && jsonElement.getAsJsonObject().get("tamin") != null;
    }

    private Weather getWeather(JsonElement jsonElement) {
        String date = jsonElement.getAsJsonObject().get("fint").getAsString();
        String station = jsonElement.getAsJsonObject().get("idema").getAsString();
        String location = jsonElement.getAsJsonObject().get("ubi").getAsString();
        Double maxTemperature = Double.valueOf(jsonElement.getAsJsonObject().get("tamax").getAsString());
        Double minTemperature = Double.valueOf(jsonElement.getAsJsonObject().get("tamin").getAsString());
        return new Weather(date, station, location, maxTemperature, minTemperature);
    }

    private String request(String url) throws IOException {
        return Jsoup.connect(url)
                .validateTLSCertificates(false)
                .timeout(6000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .header("api_key", apikey)
                .method(Connection.Method.GET)
                .maxBodySize(0)
                .execute()
                .body();
    }

    public record Area(double fromLat, double toLat, double fromLon, double toLon) {
    }
}