package com.company;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TTT on 6/27/2015.
 */
public class JsonStream implements Runnable
{
    private static final int CONN_PORT = 5555;
    private InputStream dataStream;
    private Telnet telnet;
    public JsonStream()
    {
        try {
            telnet = new Telnet(CONN_PORT);

            System.out.println("Telnet server created...");

            telnet.run();

            dataStream = telnet.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
