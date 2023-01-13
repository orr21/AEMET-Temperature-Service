package datalake;

import com.google.gson.Gson;
import datalake.POJO.Weather;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatalakeFileReader implements DatalakeReader {
    private final File root;
    private final Gson gson;

    public DatalakeFileReader(File root) {
        this.root = root;
        gson = new Gson();
    }


    @Override
    public List<Weather> readMax() {
        List<Weather> weatherList = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(root.listFiles())) {
                if (file.isFile()) {
                    weatherList.add(Files.lines(file.toPath()).map(l -> gson.fromJson(l, Weather.class))
                            .reduce(DatalakeFileReader::getMaxTemperature).get());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return weatherList;
    }

    private static Weather getMaxTemperature(Weather a, Weather b) {
        if (a.maxTemperature() > b.maxTemperature()) return a;
        else return b;
    }

    @Override
    public List<Weather> readMin() {
        List<Weather> weatherList = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(root.listFiles())) {
                if (file.isFile()) {
                    weatherList.add(Files.lines(file.toPath()).map(l -> gson.fromJson(l, Weather.class))
                            .reduce(DatalakeFileReader::getMinTemperature).get());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return weatherList;
    }

    private static Weather getMinTemperature(Weather a, Weather b) {
        if (a.minTemperature() < b.minTemperature()) return a;
        else return b;
    }
}
