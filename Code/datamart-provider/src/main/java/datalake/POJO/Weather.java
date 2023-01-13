package datalake.POJO;

public record Weather(String date, String station, String location, Double maxTemperature, Double minTemperature) {

    @Override
    public String location() {
        return location;
    }

    @Override
    public Double maxTemperature() {
        return maxTemperature;
    }

    @Override
    public Double minTemperature() {
        return minTemperature;
    }

    @Override
    public String date() {
        return date;
    }

    @Override
    public String station() {
        return station;
    }
}
