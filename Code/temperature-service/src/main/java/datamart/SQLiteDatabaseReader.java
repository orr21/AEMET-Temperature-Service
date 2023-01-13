package datamart;

import datamart.POJO.Weather;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseReader implements DatabaseReader {

    private final String url;
    private Connection connection;

    public SQLiteDatabaseReader(File root) throws SQLException {
        url = "jdbc:sqlite:" + root.getName() + "/datamart.db";
        connect();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    @Override
    public List<Weather> readMaxTemperatureBetween(String from, String to) {
        List<Weather> weatherList = new ArrayList<>();
        String sql = String.format("SELECT * from MaxTemp WHERE (DATE) BETWEEN ('%s') AND ('%s')", from, to);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                weatherList.add(new Weather(rs.getString("DATE"), rs.getString("TIME"),
                        rs.getString("PLACE"), rs.getString("STATION"), rs.getDouble("VALUE")));
            }
            return weatherList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Weather> readMinTemperatureBetween(String from, String to) {
        List<Weather> weatherList = new ArrayList<>();
        String sql = String.format("SELECT * from MinTemp WHERE (DATE) BETWEEN ('%s') AND ('%s')", from, to);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                weatherList.add(new Weather(rs.getString("DATE"), rs.getString("TIME"),
                        rs.getString("PLACE"), rs.getString("STATION"), rs.getDouble("VALUE")));
            }
            return weatherList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
