package sensor;

import datalake.POJO.Weather;

import java.io.IOException;
import java.util.List;

public interface WeatherSensor {
    List<Weather> read() throws IOException;
}
