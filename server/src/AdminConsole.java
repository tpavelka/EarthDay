import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AdminConsole {
	private int port;
	private Scanner scanner;
	private Socket socket;
	private String input;
	
	private void getGroup() {
		System.out.println("Enter a groupcodename:");
		this.input = this.scanner.nextLine();
	}
	private void getUser() {
		System.out.println("Enter a usercodename:");
		this.input = this.scanner.nextLine();
	}
	private void getEarthDay() {
		System.out.println("Enter true or false:");
		this.input = this.scanner.nextLine();
	}
	private void getAction() {
		System.out.println("What will this group do on EarthDay?");
		this.input = this.scanner.nextLine();
	}
	
	private void work() {
		System.out.println("Do not \"quit\" mid-command.");
		System.out.println("Type help for commands.");
		
		// skip a line
		System.out.println();
		
		while(!(input = scanner.nextLine()).equals("quit")) {
			if(input.equals("help")) {
				System.out.println("-----Getting Commands--- --");
				System.out.println("create a group");
				System.out.println("join a group");
				System.out.println("delete a group");
				System.out.println("leave a group");
				System.out.println("set earth day");
				System.out.println("get earth day");
				System.out.println("get members");
				System.out.println("set action");
				System.out.println("get action");
				System.out.println("has a group");
				
				// skip a line
				System.out.println();
				
			} else {
				try {
					this.socket = new Socket("127.0.0.1", port);
		            		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		            
		            		// tell the server what were doing
					out.println(input);
					
					if(input.equals("create a group")) {
						
						this.getGroup();
						out.println(input);
						
						this.getUser();
						out.println(input);
						
						// did the group already exist
						boolean hasGroup = Boolean.parseBoolean(in.readLine());
						if(!hasGroup) {
							System.out.println("Group created.");
						} else {
							System.out.println("Group already exists.");
						}
						
						// skip a line
						System.out.println();
						
					} else if(input.equals("join a group")) {
						
						this.getGroup();
						out.println(input);
						
						this.getUser();
						out.println(input);
						
						// did the group already exist
						boolean hasGroup = Boolean.parseBoolean(in.readLine());
						if(hasGroup) {
							System.out.println("Group joined.");
						} else {
							System.out.println("Group does not exist.");
						}
						
						// skip a line
						System.out.println();
						
					} else if(input.equals("delete a group")) {
						
						this.getGroup();
						out.println(input);
						
						System.out.println("Group deleted.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("leave a group")) {
						
						this.getGroup();
						out.println(input);
						
						this.getUser();
						out.println(input);
						
						System.out.println("User left group.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("set earth day")) {
						
						this.getGroup();
						out.println(input);
						
						this.getEarthDay();
						out.println(input);
						
						System.out.println("EarthDay set.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("get earth day")) {
						
						this.getGroup();
						out.println(input);
						
						System.out.println(in.readLine());
						
						System.out.println("EarthDay got.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("get members")) {
						
						this.getGroup();
						out.println(input);
						
						String member = "";
						while(!(member = in.readLine()).equals("done")) {
							System.out.println(member);
						}
						
						System.out.println("Members got.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("set action")) {
						
						this.getGroup();
						out.println(input);
						
						this.getAction();
						out.println(input);
						
						System.out.println("Action set.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("get action")) {
						
						this.getGroup();
						out.println(input);
						
						System.out.println(in.readLine());
						
						System.out.println("Action got.");
						
						// skip a line
						System.out.println();
						
						
					} else if(input.equals("has a group")) {
						
						this.getGroup();
						out.println(input);
						
						// did the group exist
						boolean hasGroup = Boolean.parseBoolean(in.readLine());
						if(hasGroup) {
							System.out.println("Group exists.");
						} else {
							System.out.println("Group does not exist.");
						}
						
						// skip a line
						System.out.println();
						
					}
					
					// close connections
					in.close();
					out.close();
					socket.close();
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		if(args[0] != null) {
			new AdminConsole(Integer.parseInt(args[0]));
		}
	}
	
	public AdminConsole(int port) {
		this.port = port;
		this.scanner = new Scanner(System.in);
		this.work();
	}
}
