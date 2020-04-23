package bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import data.Data;

@Stateless
@Path("/chat")
@LocalBean
public class ChatBean {
	
	private Map<String, User> users = new HashMap<>();
	private Map<String, User> loggedInUsers = new HashMap<>(); 
	private HashMap<UUID, Message> allMessages = new HashMap<>();

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:/jboss/exported/jms/queue/mojQueue")
	private Queue queue;

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Ok!";
	}

	@POST
	@Path("/post/{text}")
	@Produces(MediaType.TEXT_PLAIN)
	public String post(@PathParam("text") String text) {
		System.out.println("Received message: " + text);
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Ok";

	}
	
		@POST
		@Path("/register2")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.TEXT_PLAIN)
		public Response register2(User user) {
			for(User u : Data.getAll()) {
				if(u.getUsername().equals(user.getUsername())) {
						System.out.println("Username already exists!");
						return Response.status(400).build();
				}
					
					else {
						System.out.println("User successfully registered!");
						users.put(user.getUsername(), user);
						Data.getAll().add(user);
						return Response.status(200).build();
					}
				}
			
			
			System.out.println("Error!");
			return Response.status(400).build();
		}

		
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(User user) {
		for(User u : Data.getAll()) {
			if(u.getUsername().equals(user.getUsername())) {
				if(!u.getPassword().equals(user.getPassword())) {
					System.out.println("Wrong password!");
					return Response.status(400).build();
				}
				else {
					System.out.println("User successfully logged in!");
					loggedInUsers.put(user.getUsername(), user);
					Data.getLogged().add(user);
					return Response.status(200).build();
				}
			}
		}
		
		System.out.println("Username not found!");
		return Response.status(400).build();
	}
	  
		@GET
		@Path("/loggedIn")
		@Produces(MediaType.APPLICATION_JSON)
		public Collection<String> getLoggedInUsers() {
			
			List<String> usernames = new ArrayList<>();
			System.out.println("Number of logged in users: " + loggedInUsers.size());
			for (User u : loggedInUsers.values()) {
				System.out.println("Username: " + u.getUsername());
				usernames.add(u.getUsername());
			}
			Collection<String> users = usernames;
			return users;
			
		}
		
		@GET
		@Path("/registered")
		@Produces(MediaType.APPLICATION_JSON)
		public Collection<String> getRegisteredUsers() {
			
			List<String> usernames = new ArrayList<>();
			System.out.println("Number of registered users: " + users.size());
			for (User u : users.values()) {
				System.out.println("Username: " + u.getUsername());
				usernames.add(u.getUsername());
			}
			Collection<String> users = usernames;
			return users;
			
		}
		
		/*@DELETE
		@Path("/logout/{username}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response logout(@PathParam ("username") String username, User user) {
			System.out.println("--- Logout ---");
			
			for (User u : loggedInUsers.values()) {
				if (u.getUsername().equals(user.getUsername())) {
					loggedInUsers.remove(u.getUsername());
					return Response.status(200).entity("OK").build();
				}
			}
			return Response.status(200).entity("OK").build();

		}*/
		


}

