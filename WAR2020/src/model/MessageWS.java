package model;

import java.io.Serializable;
import java.util.Date;

public class MessageWS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String content;
	private String note;
	private Date date;
	
	public MessageWS() {
		super();
	}

	public MessageWS(String content, String note, Date date) {
		super();
		this.content = content;
		this.note = note;
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
