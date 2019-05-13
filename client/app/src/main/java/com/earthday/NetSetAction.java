package com.earthday;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetSetAction extends Thread {
    private String groupcodename;
    private String action;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetSetActionThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("set action");
            out.println(groupcodename);
            out.println(action);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetSetAction(String groupcodename, String action) {
        this.groupcodename = groupcodename;
        this.action = action;
    }
}
