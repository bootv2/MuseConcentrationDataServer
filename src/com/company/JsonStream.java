package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by TTT on 6/27/2015.
 */
public class JsonStream implements Runnable
{
    private Queue<String> lineQueue;
    private static final int CONN_PORT = 5555;
    private InputStream dataStream;
    private Telnet telnet;
    private boolean isFirstLine = true;
    public JsonStream()
    {
        //lineQueue = new LinkedList<String>();
        try {
            telnet = new Telnet(CONN_PORT);

            System.out.println("Telnet server created...");

            //before now, the telnet thread was started here.

            //telnetThread.

            //System.out.println("Telnet server running!");

            //dataStream = telnet.getInputStream();

            //System.out.println("DataStream set.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCommandQueue(Queue<String> cq)
    {
        lineQueue = cq;
    }

    public void setDataStream(InputStream i)
    {
        dataStream = i;
    }

    public Telnet getTelnet()
    {
        return telnet;
    }

    public Queue<String> getLineQueue()
    {
        return lineQueue;
    }

    public String getNextLine()
    {
        if(lineQueue.isEmpty())     return null;
        else                        return lineQueue.poll();
    }

    @Override
    public void run() {

        String curLine      = "";
        int lineChars       = 0;
        while(true) {
            if (dataStream != null) {
                try {
                    if (dataStream.available() > 0) {
                        lineChars = dataStream.available();
                        for (int i = 0; i < lineChars; i++) {
                            //and add the char to the current line String
                            curLine += (char) dataStream.read();
                        }
                        if (!isFirstLine) {
                            lineQueue.add(curLine);
                            System.out.println("a new line should be added to the line queue.");
                            curLine = "";
                            lineChars = 0;
                        } else {
                            curLine = "";
                            lineChars = 0;
                            isFirstLine = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    System.out.println("Setting data stream");
                    dataStream = telnet.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
