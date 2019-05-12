import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class EarthDayDatabase implements Serializable {
	private HashMap<String, Group> groups;
	public synchronized void addGroup(String groupcodename) {
		Group newgroup = new Group(groupcodename);
		this.groups.put(groupcodename, newgroup);
	}
	public synchronized void addUser(String groupcodename, String usercodename) {
		Group group = groups.get(groupcodename);
		group.addUser(usercodename);
	}
	public synchronized HashMap<String, Group> copyGroups() {
		HashMap<String, Group> copy = new HashMap<String, Group>();
		this.groups.forEach(new BiConsumer<String, Group>() {
			@Override
			public void accept(String groupcodename, Group group) {
				copy.put(groupcodename, group);
			}
		});
		return copy;
	}
	public synchronized boolean hasGroup(String groupcodename) {
		return this.groups.containsKey(groupcodename);
	}
	public synchronized void setEarthDay(String groupcodename, boolean isEarthDay) {
		Group group = this.groups.get(groupcodename);
		group.setEarthDay(isEarthDay);
	}
	public synchronized Boolean copyEarthDay(String groupcodename) {
		Group group = this.groups.get(groupcodename);
		return group.copyEarthDay();
	}
	public synchronized void removeGroup(String groupcodename) {
		this.groups.remove(groupcodename);
	}
	public synchronized void removeUser(String groupcodename, String usercodename) {
		Group group = groups.get(groupcodename);
		group.removeUser(usercodename);
	}
	public synchronized ArrayList<String> copyMembers(String groupcodename) {
		Group group = groups.get(groupcodename);
		return group.copyUsers();
	}
	public synchronized void setAction(String groupcodename, String action) {
		Group group = groups.get(groupcodename);
		group.setAction(action);
	}
	public synchronized String getAction(String groupcodename) {
		Group group = groups.get(groupcodename);
		return group.copyAction();
	}
	
	@Deprecated
	public synchronized void setGroups(HashMap<String, Group> groups) {
		this.groups = groups;
	}
	@Deprecated
	public synchronized HashMap<String, Group> getGroups() {
		return this.groups;
	}

	private static final long serialVersionUID = -742226336062463139L;
	public EarthDayDatabase() {
		this.groups = new HashMap<String, Group>();
	}
}
