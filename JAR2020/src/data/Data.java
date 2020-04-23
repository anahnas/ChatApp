package data;

import java.util.ArrayList;

import bean.User;

public class Data {
	
	private static ArrayList<User> users = new ArrayList<>();
	private static ArrayList<User> loggedIn = new ArrayList<>();
	
	static {
		users.add(new User("admin", "admin"));

		User u = new User("ana", "ana");
		users.add(u);
		loggedIn.add(u);
		
	}

	public static ArrayList<User> getAll() {
		return users;
	}

	public static void setAll(ArrayList<User> all) {
		Data.users = all;
	}

	public static ArrayList<User> getLogged() {
		return loggedIn;
	}

	public static void setLogged(ArrayList<User> loggedIn) {
		Data.loggedIn = loggedIn;
	}

	
	
	

}
