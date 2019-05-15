import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler extends Thread {
	private Date reqtime;
	private EarthDayServer server;
	
	private Socket connection;
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientHandler(EarthDayServer server, Socket connetion) {
        try {
        	// capture req vars
        	this.reqtime = new Date(System.currentTimeMillis());
        	this.connection = connetion;
			this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			this.out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true);
			
			// get the use case
			String usecase = this.in.readLine();
			
			// carry out use case
			if(usecase.equals("create a group")) {
				System.out.println("Request: create a group\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				String usercodename = this.in.readLine();
				// add groupcodename and leader
				boolean hasGroup = server.database.hasGroup(groupcodename);
				this.out.println(hasGroup);
				if(!hasGroup) {
					server.database.addGroup(groupcodename);
					server.database.addUser(groupcodename, usercodename);
				}
				
			} else if(usecase.equals("join a group")) {
				System.out.println("Request: join a group\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				String usercodename = this.in.readLine();
				// add the usercodename to groupcodename
				boolean hasGroup = server.database.hasGroup(groupcodename);
				out.println(hasGroup);
				if(hasGroup) {
					server.database.addUser(groupcodename, usercodename);
				}
				
			} else if(usecase.equals("delete a group")) {
				System.out.println("Request: delete a group\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				// detete groupcodename
				server.database.removeGroup(groupcodename);
				
			} else if(usecase.equals("leave a group")) {
				System.out.println("Request: leave a group\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				String usercodename = this.in.readLine();
				// remove the usercodename from groupcodename
				server.database.removeUser(groupcodename, usercodename);
				
			} else if(usecase.equals("set earth day")) {
				System.out.println("Request: set earth day\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				String isEarthDay = this.in.readLine();
				// set the group to isEarthDay
				server.database.setEarthDay(groupcodename, Boolean.parseBoolean(isEarthDay));
				
			} else if(usecase.equals("get earth day")) {
				System.out.println("Request: get earth day\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				// see if the group is earth day
				boolean isearthday = server.database.copyEarthDay(groupcodename);
				out.println(isearthday);
				
			} else if(usecase.equals("get members")) {
				System.out.println("Request: get members\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				// get the members of a group
				ArrayList<String> members = server.database.copyMembers(groupcodename);
				for(String member: members) {
					out.println(member);
				}
				out.println("done");
				
			} else if(usecase.equals("set action")) {
				System.out.println("Request: set action\t@ " + this.reqtime.toString());

				String groupcodename = this.in.readLine();
				String action = this.in.readLine();
				// set the action of a group
				server.database.setAction(groupcodename, action);
				
			} else if(usecase.equals("get action")) {
				System.out.println("Request: get action\t@ " + this.reqtime.toString());

				String groupcodename = this.in.readLine();
				// get the action of a group
				String action = server.database.getAction(groupcodename);
				out.println(action);
				
			} else if(usecase.equals("has a group")) {
				System.out.println("Request: has a group\t@ " + this.reqtime.toString());
				
				String groupcodename = this.in.readLine();
				// see if the group exists
				boolean hasGroup = server.database.hasGroup(groupcodename);
				out.println(hasGroup);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
