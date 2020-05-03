package bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
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

import com.google.gson.*;

import data.Data;
import ws.WSEndPoint;

@Stateless
@Path("/chat")
@LocalBean
public class ChatBean implements ChatRemote, ChatLocal {
	
	@EJB
	WSEndPoint ws;
	
	private Map<String, User> users = new HashMap<>();
	private Map<String, User> loggedInUsers = new HashMap<>(); 

	/*@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:/jboss/exported/jms/queue/mojQueue")
	private Queue queue;*/

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
		
		String jsonmess = new Gson().toJson(text);
		ws.echoTextMessage(jsonmess);
		
		
		// ws.echoTextMessage(text);
		/*try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
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

		
		@DELETE
		@Path("/loggedIn/{user}")
	    @Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response logout(@PathParam("user") String username) {
			System.out.println("--- Logout ----" + username);
			
			for (User u : loggedInUsers.values()) {
				if (u.getUsername().equals(username)) {
					loggedInUsers.remove(u.getUsername());
					System.out.println("User "+ username + "has logged out");
					return Response.status(200).entity("OK").build();
				}
			}
			
			
			System.out.println("user "+ username + "doesn't exist");
			return Response.status(400).build();
		}
	
		
		@POST
	    @Path("/messages/all")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.TEXT_PLAIN)
	    public Response sendMessageToAll(MessageDTO messageDTO)  {
			System.out.println("---Sending messages to all of the users!---");
			
			
			User sender = this.users.get(messageDTO.getSenderUsername());
			if (sender == null) {
				return Response.status(400).entity("Error").build();
			}
			
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			String jsonmsg = "";
			
			for(User u: users.values()) {
				Message message = new Message();
				message.setContent(messageDTO.getMessageContent());
				message.setSenderUser(sender);
				message.setRecieverUser(u);
				message.setSend2All(true);
				message.setSubject(messageDTO.getMessageTitle());
				Date date = new Date();
				message.setDate(date);
				u.getRecievedMessages().add(message);

				messageDTO.setDateSent(dateFormat.format(date));
				messageDTO.setReceiverUsername(u.getUsername());
				jsonmsg = new Gson().toJson(messageDTO);
				ws.echoTextMessage(jsonmsg);
				
				
				System.out.println("Sending to: " + u.getUsername());
			}
			
			System.out.println("!!!Success!!!");
			return Response.status(200).entity("Message has been sent to all users!").build();
		}
		
		@POST
	    @Path("/messages/user")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.TEXT_PLAIN)
	    public Response sendMessageToUser(MessageDTO messageDTO) {
			
			System.out.println("---Sending message to one specific user---");

			Message message = new Message();
			
			message.setContent(messageDTO.getMessageContent());
			
			User sender = this.users.get(messageDTO.getSenderUsername());
			if (sender == null) {
				System.out.println("THere is no such user: " + messageDTO.getSenderUsername());
				return Response.status(400).entity("Error").build();
			}
			
			User reciever = this.users.get(messageDTO.getReceiverUsername());
			if (reciever == null) {
				System.out.println("There is no such receiver: " + messageDTO.getReceiverUsername());
				return Response.status(400).entity("Error").build();
			}
			
			message.setSenderUser(sender);
			message.setRecieverUser(reciever);
			Date dateSent = new Date();
			message.setDate(dateSent);
			message.setSubject(messageDTO.getMessageTitle());
			message.setContent(messageDTO.getMessageContent());
			
			sender.getSentMessages().add(message);
			reciever.getRecievedMessages().add(message);
			
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			messageDTO.setDateSent(dateFormat.format(dateSent));
			
			String jsonmsg = new Gson().toJson(messageDTO);
			ws.echoTextMessage(jsonmsg);
			
			System.out.println("Message has been sent");
			return Response.status(200).entity("OK").build();
	    }
	
		
	
}

