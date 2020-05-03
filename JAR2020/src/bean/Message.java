package bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private String sender;
	// private String receiver;
	// private String dateSent;
	private Date date;
	private String subject;
	private String content;
	// private UUID id;
    // private ArrayList<String> receivers;
    private User senderUser;
    private User recieverUser;
    private boolean send2All;

	
	public Message() {
		super();
	}


	public Message(Date date, String subject, String content, User senderUser, User recieverUser) {
		super();
		this.date = date;
		this.subject = subject;
		this.content = content;
		this.senderUser = senderUser;
		this.recieverUser = recieverUser;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public User getSenderUser() {
		return senderUser;
	}


	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
	}


	public User getRecieverUser() {
		return recieverUser;
	}


	public void setRecieverUser(User recieverUser) {
		this.recieverUser = recieverUser;
	}


	public boolean isSend2All() {
		return send2All;
	}


	public void setSend2All(boolean send2All) {
		this.send2All = send2All;
	}
	
	
	
	
	
	

	
	
	

}
