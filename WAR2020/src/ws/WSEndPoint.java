package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import bean.ChatLocal;
import bean.Message;
import bean.MessageDTO;
import bean.User;
import model.MessageWS;

@Singleton
@ServerEndpoint("/ws/{username}")
@LocalBean
public class WSEndPoint {
	
	@EJB
	ChatLocal chat;
	
	@OnOpen
	public void onOpen(@PathParam("username") String username, Session session) {
		if(!sessions.contains(session)) {
    		sessions.add(session);
    		if (!userSessions.containsKey(username)) {
    			
    			List<Session> userSessions2 = new ArrayList<>();
    			userSessions2.add(session);
    			userSessions.put(username, userSessions2);
    			List<String> usernames = new ArrayList<>(userSessions.keySet());
    		    MessageWS message = new MessageWS("ulogovan", new Gson().toJson(usernames), new Date());
    			String jsonmsg = new Gson().toJson(message);
    			try {
	    			for (Session s: sessions) {
						s.getBasicRemote().sendText(jsonmsg);
	    			}
    			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
    			}
    			
    			registeredUsers.add(username);
    			
    			usernames = new ArrayList<>(registeredUsers);
    			message = new MessageWS("registrovan", new Gson().toJson(usernames), new Date());
    			jsonmsg = new Gson().toJson(message);
    			try {
	    			for (Session s: sessions) {
						s.getBasicRemote().sendText(jsonmsg);
	    			}
    			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
    			}
    		} else {
    			userSessions.get(username).add(session);
    		}
        }
    }
		
	

	
	/*@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		try {
			if(session.isOpen()) {
				for(Session s: sessions) {
					if(!s.getId().equals(session.getId())) {
						s.getBasicRemote().sendText(msg, last);
					}
				}
			}
		} catch (IOException e){
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}*/
	
	@OnMessage
	public void echoTextMessage(String msg) {
		
		System.out.println("ChatBean returned :" + chat.test());
		try {
			MessageDTO mess = new Gson().fromJson(msg, MessageDTO.class);
			List<Session> sessions = userSessions.get(mess.getReceiverUsername());
			
			if(sessions != null) {
				MessageWS msgws = new MessageWS();
				msgws.setNote("poruka");
				msgws.setContent(msg);
				String msgjson = new Gson().toJson(msgws);
			
			for(Session s: sessions) {
				System.out.println("WSEndPoint: " + msg);
				s.getBasicRemote().sendText(msgjson);
			}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@OnClose
	public void close(Session session) {
		sessions.remove(session);
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		t.printStackTrace();
	}


	public static Map<String, List<Session>> getUserSessions() {
		return userSessions;
	}

	public static void setUserSessions(Map<String, List<Session>> userSessions) {
		WSEndPoint.userSessions = userSessions;
	}
	
	static List<Session> sessions = new ArrayList<Session>();
    static Map<String, List<Session>> userSessions = new HashMap<>();
    Set<String> registeredUsers = new HashSet<String>();
	
	
}

