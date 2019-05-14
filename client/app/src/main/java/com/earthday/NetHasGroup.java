package com.earthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetHasGroup extends Thread {
    private GroupActivity activity_group;
    private String groupcodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetHasThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("has a group");
            out.println(groupcodename);

            boolean hasGroup = Boolean.parseBoolean(in.readLine());
            activity_group.hasGroup(hasGroup);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetHasGroup(GroupActivity activity, String groupcodename) {
        this.activity_group = activity;
        this.groupcodename = groupcodename;
    }
}

