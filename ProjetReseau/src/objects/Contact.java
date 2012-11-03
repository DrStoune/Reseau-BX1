package objects;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Contact implements Serializable {
	
	private int contactID;
	private String contactName;
	private InetAddress contactIp;
	private String contactIpName;
	private Socket contactSocket;
	private boolean isFriend;
	private boolean isAuthentified;
	private int contactPort;
	private SocketChannel contactSocketChannel;
	private SocketChannel contactKeyChannel;
	
	public Contact(String monContactIP, int port) {
		this.contactIpName = monContactIP;
		this.contactPort = port;
	}
	
	public Contact(){
		
	}

	public Socket getContactSocket()
	{
		return contactSocket;
	}
	
	public void setContactSocket(Socket contactSocket)
	{
		this.contactSocket =  contactSocket;
	}
	
	public void setName(String name){
		this.contactName = name;
	}
	
	public void setName(InetAddress ip){
		this.contactIp = ip;
	}
	
	public String getName(){
		return contactName;
	}
	
	public InetAddress getIP(){
		return contactIp;
	}
	
	public void setIPName(String ipName){
		this.contactIpName = ipName;
	}
	
	public String getIPName(){
		return contactIpName;
	}
	
	public boolean isFriend(){
		return isFriend;
	}
	
	public void setIsFriend(boolean b) {
		this.isFriend = b;
	}
	
	public void setContactPort(int port){
		this.contactPort = port;
	}
	
	public int getContactPort(){
		return contactPort;
	}

	public void setContactSocketChannel(SocketChannel sc) {
		this.contactSocketChannel = sc;
	}
	
	public SocketChannel getContactSocketChannel() {
		return contactSocketChannel;
	}
	
	public void setKeyChannel(SocketChannel sc){
		contactKeyChannel = sc;
	}
	
	public SocketChannel getKeyChannel(){
		return contactKeyChannel;
	}

	public void setContactIP(InetAddress inetAddress) {
		contactIp = inetAddress;
	}
	public InetAddress getContactIP() {
		return contactIp;
	}
	
	public int getContactID(){
		return contactID;
	}
	
	public void setContactID(int id){
		contactID = id;
	}

	public boolean isAuthentified() {
		return isAuthentified;
	}
	
	public void setIsAuthentified(boolean b){
		isAuthentified = b;
	}
}
