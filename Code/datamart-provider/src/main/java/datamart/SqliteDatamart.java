package datamart;

import datalake.POJO.Weather;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SqliteDatamart implements Datamart {

    private final String url;
    private Connection connection;

    public SqliteDatamart(File root) throws SQLException {
        url = "jdbc:sqlite:" + root.getName() + "/datamart.db";
        root.mkdirs();
        connect();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    @Override
    public void reload() throws SQLException {
        deleteTable("MaxTemp");
        deleteTable("MinTemp");
        createNewTable("MaxTemp");
        createNewTable("MinTemp");
    }

    private void deleteTable(String tableName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + tableName + ";";
        if (connection != null) connection.createStatement().execute(sql);
    }

    private void createNewTable(String tableName) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "	DATE text NOT NULL,\n"
                + "	TIME text NOT NULL,\n"
                + "	PLACE text NOT NULL,\n"
                + "	STATION text NOT NULL,\n"
                + "	VALUE float NOT NULL\n"
                + ");";
        if (connection != null) connection.createStatement().execute(sql);
    }

    @Override
    public void storeMax(List<Weather> maxWeatherList) throws SQLException {
        store(maxWeatherList, "INSERT INTO MaxTemp(date,time,place,station,value) VALUES(?,?,?,?,?)");
    }

    @Override
    public void storeMin(List<Weather> minWeatherList) throws SQLException {
        store(minWeatherList, "INSERT INTO MinTemp(date,time,place,station,value) VALUES(?,?,?,?,?)");
    }

    private void store(List<Weather> weatherList, String sql) throws SQLException {
        for (Weather weather : weatherList) {
            connection.setAutoCommit(false);
            prepareStatement(weather, sql);
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    private void prepareStatement(Weather weather, String sql) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, getDate(weather));
        pstmt.setString(2, getTime(weather));
        pstmt.setString(3, weather.location());
        pstmt.setString(4, weather.station());
        if (sql.contains("MaxTemp")) pstmt.setDouble(5, weather.maxTemperature());
        else pstmt.setDouble(5, weather.minTemperature());
        pstmt.executeUpdate();
    }

    private String getDate(Weather weather) {
        LocalDate ld = LocalDate.parse(weather.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        return ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getTime(Weather weather) {
        LocalDateTime ldt = LocalDateTime.parse(weather.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        return ldt.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
