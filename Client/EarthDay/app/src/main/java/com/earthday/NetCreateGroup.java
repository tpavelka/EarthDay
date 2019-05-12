package com.earthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetCreateGroup extends Thread {
    private CreateActivity activity_create;
    private String groupcodename;
    private String usercodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetCreateThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("create a group");
            out.println(groupcodename);
            out.println(usercodename);

            boolean hasGroup = Boolean.parseBoolean(in.readLine());
            activity_create.hasGroup(hasGroup);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetCreateGroup(CreateActivity activity, String groupcodename, String usercodename) {
        this.activity_create = activity;
        this.groupcodename = groupcodename;
        this.usercodename = usercodename;
    }
}
