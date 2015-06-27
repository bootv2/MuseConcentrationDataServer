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
    public JsonStream()
    {
        lineQueue = new LinkedList<String>();
        try {
            telnet = new Telnet(CONN_PORT);

            System.out.println("Telnet server created...");

            telnet.run();

            dataStream = telnet.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        while(true)
        {
            try {
                if(dataStream.available() > 0)
                {
                    lineChars       = dataStream.available();
                    for(int i = 0; i < lineChars; i++) {
                        //and add the char to the current line String
                        curLine     += (char) dataStream.read();
                    }
                    lineQueue.add(curLine);
                    System.out.println("a new line should be added to the line queue.");
                    System.out.println("line: " + curLine);
                    curLine         = "";
                    lineChars       = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
