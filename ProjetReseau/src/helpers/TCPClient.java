package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import Interfaces.Profil;

import objects.Contact;
import objects.Me;

//cette classes envoi des requetes aux serveur et recupere les reponses
public class TCPClient {

	protected static Set<Contact> distants;
	protected static Set<Contact> distantsConnectes;
	protected static Set<Contact> distantsNonConnectes;
	public int m_receiveBufferSize;
	public Charset m_charset;
	public CharsetEncoder m_encoder;
	public CharsetDecoder m_decoder;
	static int port ;
		
	public TCPClient() {
		//utiliser une table d'association id -> objet a la place
		distants = new HashSet<Contact>();
		distantsConnectes = new HashSet<Contact>();
		distantsNonConnectes = new HashSet<Contact>();
		m_receiveBufferSize = 1000;
		m_charset = Charset.forName("UTF-8");
		m_encoder = m_charset.newEncoder();
		m_decoder = m_charset.newDecoder();
	}

	//create a socket to commmunicate with the contact and add it to distants list
	public static void connect(Contact contact, Me moi) {
		try {
			System.out.println("Try to connect to " + contact.getIPName());
			Socket socket = new Socket(contact.getIPName(), contact.getContactPort());
			contact.setContactSocket(socket);
			System.out.println("TCPClient connect() AUTHENTIFICATION to " +contact.getIPName()+ ":"+contact.getContactPort());
			distantsConnectes.add(contact);
			if (!send(contact.getContactID(), "AUTHENTIFICATION "+ moi.getMeId() + " " + moi.getFullName())){
				distantsNonConnectes.add(contact);
				distantsConnectes.remove(contact);
			}
		} catch (IOException e) {
			System.out.println("Erreur dans TCPClient connect() " + e);
			distantsNonConnectes.add(contact);
			distantsConnectes.remove(contact);
			e.printStackTrace();
		}
	}

	//send the command and wait for response, select the contact in distants list by compare the id
	//TODO : implement an out of time error
	public static boolean send(int contactID, String command) {
		try{
			Set<Contact> clientsSet = distantsConnectes;
			Iterator<Contact> it = clientsSet.iterator();
			Contact contact;
				while (it.hasNext()) {
					contact = it.next();
					if (contact.getContactID() == contactID){
			
						OutputStream os = contact.getContactSocket().getOutputStream();
						PrintStream ps = new PrintStream(os, false, "utf-8");
						System.out.println("TCPClient send() command : " + command);
						ps.print(command);
						//ps.flush();
						
						//essai de lire directement la reponse
						InputStream is = contact.getContactSocket().getInputStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
						String line = br.readLine();
						System.out.println("TCPClient send() response : " + line);
					}
				}
				return true;
		} catch (IOException e) {
			System.out.println("Erreur d'envoi" + e);
			e.printStackTrace();
			return false;
		}
	}

	// envoyer la command voulu a tous les contacts
	public void sendToAll(String command, String content) {
		try {
			Me me = Profil.getMe();
			connectWhithNonConnecte(me);
			String name = me.getFullName();
			System.out.println("TCPClient send() command + content : " + command + " " + content);
			Set<Contact> clientsSet = this.distantsConnectes;
			Iterator<Contact> it = clientsSet.iterator();
			Contact contact;
			while (it.hasNext()) {
				contact = it.next();
				String newPost = content;
				String target = contact.getIPName();
				Socket hSocket = contact.getContactSocket();
				OutputStream os = contact.getContactSocket().getOutputStream();
				PrintStream ps = new PrintStream(os, false, "utf-8");
				ps.print(command+content);
				ps.flush();
			}

		} catch (IOException e) {
			System.out.println("Erreur d'envoi" + e);
			e.printStackTrace();
		}
	}

	//connect to the friend list
	public void connectWhithFriends(Me moi) {
		Set<Contact> friends = CacheHelper.selectAllContacts();
		System.out.println("Try to connect to all friends");
		if (friends != null) {
			Iterator<Contact> it = friends.iterator();
			while (it.hasNext()) {
				Contact friend = it.next();
				connect(friend,moi);
				System.out.println("Connexion à : " + friend.getContactID());
			}
		}
	}
	
	public void connectWhithNonConnecte(Me moi) {
		Set<Contact> friends = distantsNonConnectes;
		System.out.println("Try to connect to non connectes");
		if (friends != null) {
			Iterator<Contact> it = friends.iterator();
			while (it.hasNext()) {
				Contact friend = it.next();
				connect(friend,moi);
				System.out.println("Connexion à : " + friend.getContactID());
			}
		}
	}
}
