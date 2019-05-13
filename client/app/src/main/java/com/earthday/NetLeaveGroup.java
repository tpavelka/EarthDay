package com.earthday;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class NetLeaveGroup extends Thread {
    private String groupcodename;
    private String usercodename;

    @Override
    public void run() {
        System.out.println("Doing connection!");
        this.setName("NetLeaveThread");
        String host = MainActivity.host;
        int port = MainActivity.port;
        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("leave a group");
            out.println(groupcodename);
            out.println(usercodename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetLeaveGroup(String groupcodename, String usercodename) {
        this.groupcodename = groupcodename;
        this.usercodename = usercodename;
    }
}
