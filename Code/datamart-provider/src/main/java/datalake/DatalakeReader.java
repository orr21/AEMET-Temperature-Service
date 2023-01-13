package datalake;

import datalake.POJO.Weather;

import java.util.List;

public interface DatalakeReader {

    List<Weather> readMax();

    List<Weather> readMin();
}
