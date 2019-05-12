package com.earthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetJoinGroup extends Thread {
    private JoinActivity activity_join;
    private String groupcodename;
    private String usercodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetJoinThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("join a group");
            out.println(groupcodename);
            out.println(usercodename);

            boolean hasGroup = Boolean.parseBoolean(in.readLine());
            activity_join.hasGroup(hasGroup);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetJoinGroup(JoinActivity activity, String groupcodename, String usercodename) {
        this.activity_join = activity;
        this.groupcodename = groupcodename;
        this.usercodename = usercodename;
    }
}
