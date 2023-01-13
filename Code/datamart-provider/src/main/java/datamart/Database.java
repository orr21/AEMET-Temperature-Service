package datamart;

import datalake.POJO.Weather;

import java.sql.SQLException;
import java.util.List;

public interface Database {

    void storeMax(List<Weather> weatherList) throws SQLException;

    void storeMin(List<Weather> weatherList) throws SQLException;

    void reload() throws SQLException;
}
