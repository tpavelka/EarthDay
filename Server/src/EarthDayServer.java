
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

//the xml for creating the empty EarthDayDatabase object
/*
<?xml version="1.0" encoding="UTF-8"?>
<java version="1.8.0_201" class="java.beans.XMLDecoder">
	<object class="EarthDayDatabase"/>
</java>
*/

public class EarthDayServer {
	private XMLEncoder encoder;
	private XMLDecoder decoder;
	
	private Timer savetimer;
	
	private ServerSocket serversocket;
	public EarthDayDatabase database;
	
	public EarthDayServer() {
		// starting the server, get the database
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Database.xml"));
			this.decoder = new XMLDecoder(bis);
			this.database = (EarthDayDatabase) this.decoder.readObject();
			this.decoder.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		// start the database save timer every 5:00 mins = 300000
		this.savetimer = new Timer();
		this.savetimer.scheduleAtFixedRate(new SaveTask(), 20000, 20000);
		
		try {
			// max port num is 65535
			int port = ;
			serversocket = new ServerSocket(port);
			serversocket.setReuseAddress(true);
			while(true) {
				Socket connection = serversocket.accept();
				ClientHandler ch = new ClientHandler(this, connection);
				ch.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serversocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new EarthDayServer();
	}
	
	private class SaveTask extends TimerTask {
		@Override
		public void run() {
			BufferedOutputStream bos;
			try {
				bos = new BufferedOutputStream(new FileOutputStream("Database.xml"));
				encoder = new XMLEncoder(bos);
				encoder.writeObject(database);
				encoder.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
