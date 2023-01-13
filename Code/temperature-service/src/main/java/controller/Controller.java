package controller;


import datamart.DatamartReader;
import datamart.SqliteDatamartReader;
import webservice.SparkWebService;
import webservice.WebService;

import java.io.File;
import java.sql.SQLException;

public class Controller {
    private final WebService webservice;

    public Controller(File rootDatamart) throws SQLException, InterruptedException {
        Thread.sleep(10000);
        DatamartReader datamart = new SqliteDatamartReader(rootDatamart);
        webservice = new SparkWebService(datamart);
    }

    public void run() {
        System.out.println("Service Started");
        webservice.run();
    }
}
