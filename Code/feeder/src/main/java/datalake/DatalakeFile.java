package datalake;

import com.google.gson.Gson;
import datalake.POJO.Weather;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatalakeFile implements Datalake {
    private final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
    private final String EXTENSION = ".events";
    private final File root;
    private final Gson gson;

    public DatalakeFile(File root) {
        this.root = root;
        root.mkdirs();
        gson = new Gson();
    }

    @Override
    public List<Weather> read(LocalDate date) {
        File file = new File(root, formatter.format(date) + EXTENSION);
        try {
            return Files.lines(file.toPath())
                    .map(l -> gson.fromJson(l, Weather.class))
                    .toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void write(List<Weather> events) {
        events.forEach(e -> {
            try {
                    Files.writeString(fileOf(e.date()).toPath(), gson.toJson(e) + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private File fileOf(String ts) {
        LocalDateTime datetime = LocalDateTime.parse(ts, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String formatedDate = datetime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new File(root, formatedDate + EXTENSION);
    }
}
