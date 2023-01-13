package controller;


import datamart.DatabaseReader;
import datamart.SQLiteDatabaseReader;
import webservice.SparkWebService;
import webservice.WebService;

import java.io.File;
import java.sql.SQLException;

public class Controller {
    private final WebService webservice;

    public Controller(File rootDatamart) throws SQLException {
        DatabaseReader database = new SQLiteDatabaseReader(rootDatamart);
        webservice = new SparkWebService(database);
    }

    public void run() {
        System.out.println("Service Started");
        webservice.run();
    }
}
