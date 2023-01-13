package datamart;


import datamart.POJO.Weather;

import java.util.List;

public interface DatabaseReader {

    List<Weather> readMaxTemperatureBetween(String from, String to);

    List<Weather> readMinTemperatureBetween(String from, String to);
}
