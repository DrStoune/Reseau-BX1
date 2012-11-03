package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Me implements Serializable {
	private String meFirstName;
	private String meLastName;
	private static ArrayList<Contact> friends;
	private String birthday;
	private int meId;
	private int lastIDContact;
	private int mePort;
	
	public Me(){	
		meId = 0;
		friends = new ArrayList<Contact>();
	}
	
	public String getFirstName(){
		return meFirstName;
	}
	
	public void setFirstName(String name){
		meFirstName = name;
	}
	public String getLastName(){
		return meLastName;
	}
	
	public void setLastName(String name){
		meLastName = name;
	}
	
	public String getFullName(){
		return meFirstName + " " + meLastName;
	}
	
	public static ArrayList getFriends(){
		return friends;
	}
	
	public void setBirthday(String date){
		this.birthday = date;
	}
	
	public String getBirthday(){
		return birthday;
	}
	
	public static void addFriend(Contact contact){
		friends.add(contact);
	}
	
	public static void removeFriend(Contact contact){
		friends.remove(contact);
	}

	public int getLastIDContact() {
		return lastIDContact;
	}
	
	public void incrementLastIdContact(){
		lastIDContact++;
	}
	
	public void setLastIdContact(int id){
		lastIDContact = id;
	}
	
	public int getMeId() {
		return meId;
	}
	
	public void setMeId(int id){
		meId = id;
	}
	
	public int getMePort(){
		return mePort;
	}
	
	public void setPort(int port){
		mePort = port;
	}
}
