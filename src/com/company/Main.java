package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Telnet telnet;
        try {
            telnet = new Telnet(5555);
            telnet.run();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
