package bean;

import javax.ejb.Remote;

@Remote
public interface ChatRemote {
	public String post(String text);

}
