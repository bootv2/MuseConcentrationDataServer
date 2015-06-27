package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by TTT on 6/27/2015.
 */
public class DatabaseController
{
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DatabaseController()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://94.209.43.230:3306/muse_data", "muse", "data");


            System.out.println("Database connection: " + connect);



            // Statements allow to issue SQL queries to the database
            //statement = connect.createStatement();
        }
        catch(SQLException e)
        {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String queryFromDataPoint(EEGDataPoint p)
    {
        String result = "INSERT INTO datapoints(value, time)";
        result += "VALUES(" + p.getConcentrationValue() + ", " + p.getTime() + ");";
        return result;
    }

    public void insertDataPoint(EEGDataPoint p)
    {
        try {
            statement = connect.createStatement();
            statement.execute(queryFromDataPoint(p));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
