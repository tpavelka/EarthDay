package com.earthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetGetEarthDay extends Thread {
    private GroupActivity activity_group;
    private String groupcodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetGetEarthDayThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("get earth day");
            out.println(groupcodename);

            String isearthday = in.readLine();
            activity_group.setEarthDay(Boolean.parseBoolean(isearthday));
            activity_group.increment();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetGetEarthDay(GroupActivity activity_group, String groupcodename) {
        this.activity_group = activity_group;
        this.groupcodename = groupcodename;
    }
}
