package com.company;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private String next;
    private DatabaseController dbController;
    private List<EEGDataPoint> dataList = new ArrayList<EEGDataPoint>();
    private EEGDataPoint currentDataPoint;
    private Chronotype chronotype = new Chronotype();

    public static void main(String[] args) {
        Main m = new Main();

        m.run();

    }

    public void run()
    {
        System.out.println("[i]Attempting to connect to database before starting server.");
        DatabaseController dbController = new DatabaseController();

        System.out.println("Attempting to start synched threaded telnet server");
        JsonCommandQueueManager q = new JsonCommandQueueManager();

        synchronized (this) {
            try {
                this.wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //q.sendCommand("TEST!!!!!!!!!!!!!");


            System.out.println("CommandQueue created");
            while (true) {
                next = q.readNext();
                if (next != null) {
                    System.out.println("Line: " + next);
                    if(next.equals("EndStream")) q.sendCommand("Uw concentratiepiek ligt ongeveer op " + calculateResults());
                    else
                    {
                        try {
                            currentDataPoint = new EEGDataPoint(next);
                            dataList.add(currentDataPoint);
                        } catch (JSONException e) {
                            System.out.println("JSON INVALID!!!!!! INVALID JSON!!!!!! CHECK JSON: " + next);
                            e.printStackTrace();
                        }

                    }
                } else {
                    try {
                        this.wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String calculateResults()
    {
        chronotype.setDataPointList(dataList);
        int chrono = chronotype.determineChronotype();
        String type = "";

        switch(chrono)
        {
            case 0:
                type = "ochtend(10 uur(10am))";
                break;

            case 1:
                type = "middaguur(12 uur(12am))";
                break;

            case 2:
                type = "middag(14 uur(2pm))";
                break;

            case 3:
                type = "namiddag(16 uur(4pm))";
                break;

            case 4:
                type = "avond(18 uur(6pm))";
                break;

            case 5:
                type = "avond(20 uur(8pm))";
                break;

            case 6:
                type = "late avond(22 uur(10pm))";
                break;

            default:
                System.out.println("[X] There has been an error in chronotype calculation");
                break;
        }
        return type;
    }

}
