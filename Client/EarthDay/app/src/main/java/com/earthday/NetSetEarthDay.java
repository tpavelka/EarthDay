package com.earthday;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetSetEarthDay extends Thread {
    private String groupcodename;
    private String bool;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetJoinThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("set earth day");
            out.println(groupcodename);
            out.println(bool);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetSetEarthDay(String groupcodename, String bool) {
        this.groupcodename = groupcodename;
        this.bool = bool;
    }
}
