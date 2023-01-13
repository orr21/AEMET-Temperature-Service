package datalake;

import datalake.POJO.Weather;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface Datalake {
    List<Weather> read(LocalDate date) throws IOException;

    void write(List<Weather> events);
}
