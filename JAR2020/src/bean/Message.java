package bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sender;
	// private String receiver;
	private LocalDateTime time;
	private String subject;
	private String content;
	private UUID id;
    private ArrayList<String> receivers;
	
	public Message() {
		super();
	}

	public Message(String sender, ArrayList<String> receivers, LocalDateTime time, String subject, String content, UUID id) {
		super();
		this.sender = sender;
		this.receivers = receivers;
		this.time = time;
		this.subject = subject;
		this.content = content;
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}


	public ArrayList<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(ArrayList<String> receivers) {
		this.receivers = receivers;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Message [sender=" + sender + ", time=" + time + ", subject=" + subject + ", content=" + content
				+ ", id=" + id + ", receivers=" + receivers + "]";
	}

	
	
	

}
