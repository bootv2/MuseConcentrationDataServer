package com.company;

import java.io.IOException;

public class Main {

    private String next;

    public static void main(String[] args) {
        Main m = new Main();

        m.run();

    }

    public void run()
    {
        System.out.println("Attempting to start synched threaded telnet server");
        JsonCommandQueue q = new JsonCommandQueue();

        synchronized (this) {
            try {
                this.wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("CommandQueue created");
            while (true) {
                next = q.readNext();
                if (next != null) {
                    System.out.println("Line: " + next);
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

}
