package com.company;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by TTT on 6/27/2015.
 */
public class JsonCommandQueue
{
    private Queue<String> liveCommandQueue;
    private Queue<String> readableCommandQueue;
    private Thread tTelnet;
    private Thread tJsonStream;
    private JsonStream jStream;
    private Telnet telInstance;

    public JsonCommandQueue()
    {
        jStream = new JsonStream();
        telInstance = jStream.getTelnet();

        System.out.println("[S]About to start telnet thread");


        System.out.println("[Y]Telnet thread started!");
        telInstance.run();
        System.out.println("[Y]Telnet Thread finished!");

        System.out.println("[Y] Telnet thread started without problems!\nStarting JSONStream thread...");
        tJsonStream = new Thread(jStream);
        System.out.println("JSONStream Started!");
        try {
            jStream.setDataStream(telInstance.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSONStreams dataStream has been set!");

        liveCommandQueue = new LinkedList<String>();

        jStream.setCommandQueue(liveCommandQueue);

        System.out.println("JSONStream commandQueue set");

        readableCommandQueue = new LinkedList<String>();

        System.out.println("Starting JSONStream thread");
        tJsonStream.start();
    }

    public String readNext()
    {
        //synchQueues();
        synchronized (tJsonStream) {
            if (liveCommandQueue.isEmpty()) return null;
            else return liveCommandQueue.poll();
        }
    }

    private void synchQueues()
    {
        synchronized (tJsonStream) {
            System.out.println("[i]Attempting to synchronise queues...");
            try {
                //if(tJsonStream.isAlive()) System.out.println("Json stream still alive");
                System.out.println("[T] telling JSONstream thread to wait");
                tJsonStream.wait();
                //tTelnet.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("[E]Couldnt make the json or telnet thread wait.");
            }

            while (!liveCommandQueue.isEmpty()) {
                readableCommandQueue.add(liveCommandQueue.poll());
            }
            tJsonStream.notify();
            System.out.println("[Y]Synched!");
            //tTelnet.notify();
        }
    }

    public void addLiveCommand(String c)
    {
        liveCommandQueue.add(c);
    }
}
