package runnable;

import controller.Controller;
import sensor.AemetWeatherSensor.Area;

import java.io.File;

public class FeederMain {

    private final static File root = new File("datalake/");

    public static void main(String[] args) {
        String apikey = args[0];
        Area area = new Area(Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Double.parseDouble(args[4]));
        new Controller(apikey, root, area).run();
    }
}

