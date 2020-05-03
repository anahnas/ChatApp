package bean;

import java.io.Serializable;

public class MessageDTO implements Serializable {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private String senderUsername;
		
		private String receiverUsername;
		
		private String messageContent;
		
		private String messageTitle;
		
		private String dateSent;
		
		public MessageDTO() {
			
		}
		
		public MessageDTO(String sender, String receiver, String content, String messageTitle, String dateSent) {
			this.senderUsername = sender;
			this.receiverUsername = receiver;
			this.messageContent = content;
			this.messageTitle = messageTitle;
			this.dateSent = dateSent;
		}
		
		public MessageDTO(String sender, String receiver, String content, String messageTitle) {
			this.senderUsername = sender;
			this.receiverUsername = receiver;
			this.messageContent = content;
			this.messageTitle = messageTitle;
		}
		
		public String getSenderUsername() {
			return senderUsername;
		}
		public void setSenderUsername(String senderUsername) {
			this.senderUsername = senderUsername;
		}
		public String getReceiverUsername() {
			return receiverUsername;
		}
		public void setReceiverUsername(String recieverUsername) {
			this.receiverUsername = recieverUsername;
		}
		public String getMessageContent() {
			return messageContent;
		}
		public void setMessageContent(String messageContent) {
			this.messageContent = messageContent;
		}

		public String getMessageTitle() {
			return messageTitle;
		}

		public void setMessageTitle(String messageTitle) {
			this.messageTitle = messageTitle;
		}

		public String getDateSent() {
			return dateSent;
		}

		public void setDateSent(String dateSent) {
			this.dateSent = dateSent;
		}

}
