package com.earthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class NetGetMembers extends Thread {
    private GroupActivity activity_group;
    private String groupcodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetGetMembersThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            ArrayList<String> members = new ArrayList<String>();

            out.println("get members");
            out.println(groupcodename);

            String nextmember;
            while(!(nextmember = in.readLine()).equals("done")) {
                members.add(nextmember);
            }

            activity_group.setMembers(members);
            activity_group.increment();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetGetMembers(GroupActivity activity_group, String groupcodename) {
        this.activity_group = activity_group;
        this.groupcodename = groupcodename;
    }
}
