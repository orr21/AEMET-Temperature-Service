package controller;

import datalake.Datalake;
import datalake.DatalakeFile;
import datalake.POJO.Weather;
import sensor.AemetWeatherSensor;
import sensor.AemetWeatherSensor.Area;
import sensor.WeatherSensor;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final WeatherSensor sensor;
    private final Datalake datalake;

    public Controller(String apikey, File root, Area area) {
        sensor = new AemetWeatherSensor(apikey, area);
        datalake = new DatalakeFile(root);
    }

    public void run() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 3600 * 1000);
    }

    private void execute() throws IOException {
        System.out.println("Feeder Started " + LocalDateTime.now());
        List<Weather> todayEvents = datalake.read(LocalDate.now());
        List<Weather> yesterdayEvents = datalake.read(LocalDate.now().minusDays(1));
        List<Weather> events = Stream.concat(todayEvents.stream(), yesterdayEvents.stream()).toList();
        List<Weather> newEvents = sensor.read().stream()
                .filter(e -> !isOnFile(e, events))
                .collect(Collectors.toList());
        System.out.println(newEvents);
        datalake.write(newEvents);
        System.out.println("Feeder Completed " + LocalDateTime.now());
    }

    private boolean isOnFile(Weather e, List<Weather> events) {
        return events.stream().anyMatch(event -> event.date().equals(e.date()) && event.station().equals(e.station()));
    }
}
