import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class Group implements Serializable {
	private String name;
	public String copyName() {
		return new String(this.name);
	}
	@Deprecated
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean earthDay;
	public Boolean copyIsEarthDay() {
		return new Boolean(this.earthDay);
	}
	@Deprecated
	public boolean getEarthDay() {
		return this.earthDay;
	}
	public Boolean copyEarthDay() {
		return new Boolean(this.earthDay);
	}
	public void setEarthDay(boolean earthDay) {
		this.earthDay = earthDay;
	}
	
	private String action;
	public String copyAction() {
		return new String(this.action);
	}
	@Deprecated
	public String getAction() {
		return this.action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	private ArrayList<String> users;
	public void addUser(String name) {
		this.users.add(name);
	}
	public ArrayList<String> copyUsers() {
		ArrayList<String> copy = new ArrayList<String>();
		for(String user: users) {
			copy.add(user);
		}
		return copy;
	}
	@Deprecated
	public ArrayList<String> getUsers() {
		return this.users;
	}
	@Deprecated
	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
	public void removeUser(String name) {
		this.users.remove(name);
	}
	
	private static final long serialVersionUID = -3051652927724406707L;
	public Group() {
		this.name = "";
		this.earthDay = false;
		this.action = "default";
		this.users = new ArrayList<String>();}
	public Group(String name) {
		this.name = name;
		this.earthDay = false;
		this.action = "default";
		this.users = new ArrayList<String>();
	}
}
