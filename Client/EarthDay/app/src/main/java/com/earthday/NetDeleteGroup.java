package com.earthday;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetDeleteGroup extends Thread {
    private String groupcodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetDeleteThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("delete a group");
            out.println(groupcodename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetDeleteGroup(String groupcodename) {
        this.groupcodename = groupcodename;
    }
}
