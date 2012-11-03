package helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import Interfaces.Profil;

import objects.Contact;
import objects.Me;

public class TCPServer extends Thread{
	
	public int m_receiveBufferSize;
	public Hashtable<Integer,Contact> m_contacts;
	public Hashtable<SocketChannel,Contact> m_contactsNonValide;
	public Set<SelectionKey> keys;
	public Charset m_charset;
	public CharsetEncoder m_encoder;
	public CharsetDecoder m_decoder;
	int port ;
	
	public TCPServer(int port)
	{
		this.port = port;
		m_receiveBufferSize = 1000;
		m_contacts = new Hashtable<Integer,Contact>();
		m_contactsNonValide = new Hashtable<SocketChannel,Contact>();
		m_charset = Charset.forName("UTF-8");
		m_encoder = m_charset.newEncoder();
		m_decoder = m_charset.newDecoder();
	}
	
	public void run()
	{
		// Create listening socket
		ServerSocketChannel ssc;
		try {
			ssc = ServerSocketChannel.open();
			ServerSocket socket = ssc.socket();
			socket.bind(new InetSocketAddress(port));
			socket.setReuseAddress(true);
			System.out.println("Listening to port " + port);
		
			// Create selector
			Selector selector = Selector.open();
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			// Create name generator
			Random random = new Random(System.currentTimeMillis());
			
			while (true)
			{
				selector.select();
				keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while(it.hasNext())
				{
					SelectionKey key = (SelectionKey) it.next();
					if(key.isAcceptable())
					{
						// Create clientl
						Contact contact = new Contact();
						Socket hSocket = socket.accept();
						contact.setContactIP(hSocket.getInetAddress());
						contact.setContactSocket(hSocket);
						
						// Register client socket & channel
						SocketChannel sc = contact.getContactSocket().getChannel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ, contact);
						contact.setContactSocketChannel(sc);
						m_contactsNonValide.put(sc,contact);
					}
					else if(key.isReadable())
					{
						SocketChannel channel = (SocketChannel) key.channel();
						Contact contact = (Contact) key.attachment();
						ByteBuffer bb = ByteBuffer.allocate(m_receiveBufferSize);
						contact.setKeyChannel(channel);
						int readSize = channel.read(bb);
						if(readSize < 0) // Client disconnected
						{
							key.cancel();
							m_contactsNonValide.remove(channel);
						}
						else // Execute command
						{
							System.out.println("TCPServer run() command reçu");
							bb.position(0);
							String command = m_decoder.decode(bb).toString().substring(0, readSize);
							System.out.println("TCPServer run() command reçu : " + command);
							executeCommand(contact, command);
						}
					}
				}
				keys.clear();
			}
		} catch (IOException e) {
			System.out.println("Erreur dans le thread serveur " + e);
			e.printStackTrace();
		}
	}
	
	public void authentification(Contact contact) throws IOException{
		System.out.println("TCPServer authentification()");
	}
	
	public void executeCommand(Contact contact, String command) throws IOException
	{
		Contact contact2 = m_contactsNonValide.get(contact.getContactSocketChannel());
		String commandCode = command.split(" ")[0];
		System.out.println("TCPServer executeCommand() ligne complete : |" + command + "|");
		System.out.println("TCPServer executeCommand() commandCode : |" + commandCode + "|");
		if(!contact2.isAuthentified()){
			System.out.println("TCPServer run() Vous n'etes pas authentifié");
			System.out.println("TCPServer run() contact2.getContactId "+ contact2.getContactID());
			//send(contact2, "Vous n'etes pas authentifié");
			if(commandCode.equals("AUTHENTIFICATION"))
				executeAUTHENTIFICATION(contact2, command.substring(Math.min(command.length(), 17)));
		}else{
			if(commandCode.equals("WHO"))
				askWHO(contact);
			else if(commandCode.equals("POST"))
				executePOST(contact, command.substring(Math.min(command.length(), 4)));
			else{
				send(contact, "Error : invalid command\n");
				System.out.println("TCPServer executeCommand() Error : invalid command");
			}
		}
	}
	
	
	private void executeAUTHENTIFICATION(Contact contact, String substring) {
		contact.setIsAuthentified(true);
		String idCode = substring.split(" ")[0];
		
		contact.setContactID(Integer.parseInt(idCode));
		contact.setName(substring.substring(Math.min(substring.length(), 3)));
		m_contactsNonValide.remove(contact.getContactSocketChannel());
		m_contactsNonValide.put(contact.getContactSocketChannel(), contact);
		try {
			ByteBuffer bb = m_encoder.encode(CharBuffer.wrap("OK c'est bon"+"\r\n"));
			System.out.println("TCPServer executeAUTHENTIFICATION() envoi de : " + "OK c'est bon");
			contact.getContactSocketChannel().write(bb);
		} catch (IOException e) {
			System.out.println("Erreur dans executeAUTHENTIFICATION() : " + e);
			e.printStackTrace();
		}
	}

	//repond a la question WHO, 
	//Attention !! Bien finir chaque reponse par un retour a la ligne, sinon elle ne sera jamais lu par notre client
	private void askWHO(Contact contact) {
		System.out.println("TCPServer askWHO() contactID : " + contact.getContactID());
		Me me = Profil.getMe();
		ByteBuffer bb ;
		try {		
			if(contact.isFriend()){
				bb = m_encoder.encode(CharBuffer.wrap("201 "+me.getMeId() + "," +me.getFirstName() + "," + me.getLastName() + ";" + me.getBirthday() +"\r\n"));
				System.out.println("TCPServer askWHO() isFriend : envoi de : " + "201 "+me.getMeId() + "," + me.getFirstName() +"," + me.getLastName() + ";" + me.getBirthday());
				contact.getContactSocketChannel().write(bb);
			}else{
				bb = m_encoder.encode(CharBuffer.wrap("211 " +me.getMeId() + "," + me.getFirstName() +"," + me.getLastName() +"\r\n"));
				System.out.println("TCPServer askWHO() notFriend : envoi de : " + "211 " +me.getMeId() + "," + me.getFirstName() +"," + me.getLastName());
				contact.getContactSocketChannel().write(bb);				
			}
		} catch (IOException e) {
			System.out.println("Erreur dans receiveWHO() : " + e);
			e.printStackTrace();
		}
	}

	public void send(Contact contact, String str) throws IOException
	{
		try {
			System.out.println("TCPServer send() str : " + str);
			ByteBuffer bb = m_encoder.encode(CharBuffer.wrap(str));
			contact.getContactSocketChannel().write(bb);
		} catch (IOException e) {
			System.out.println("Erreur dans send() : " + e);
			e.printStackTrace();
		}
	}
	
	//n'a pas encore son utilité, mais peut envoyer une ligne a tous les contacts brancher sur ce serveur
	public void sendToAll(String str) throws IOException
	{
		ByteBuffer bb = m_encoder.encode(CharBuffer.wrap(str));
		
		for (Contact contact : m_contacts.values() ) {
			contact.getContactSocket().getChannel().write(bb);
		}
	}
	
	//reception d'un post
	public void executePOST(Contact contact, String message) throws IOException
	{
		Contact contact2 = m_contactsNonValide.get(contact.getContactSocketChannel());
		
		Profil.displayNewPost(contact2.getName() + " : " +  message + "\r\n");
	}
}
