package com.earthday;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NetGetAction extends Thread {
    private Activity activity;
    private String groupcodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetGetActionThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("get action");
            out.println(groupcodename);

            String action = in.readLine();
            if(activity instanceof ManageActivity) {
                ManageActivity act = (ManageActivity) activity;
                act.action(action);

            } else if(activity instanceof GroupActivity) {
                GroupActivity act = (GroupActivity) activity;
                act.action(action);
                act.increment();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetGetAction(GroupActivity activity, String groupcodename) {
        this.activity = activity;
        this.groupcodename = groupcodename;
    }
    public NetGetAction(ManageActivity activity, String groupcodename) {
        this.activity = activity;
        this.groupcodename = groupcodename;
    }
}
