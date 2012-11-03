package helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import objects.Contact;
import objects.Me;
import objects.Post;

public class CacheHelper {

	private static String nameMyPostFile = "Account/mesPosts";
	private static String nameContactsFile = "Account/mesContacts";
	private static String nameTheirPostFile = "Account/leursPosts";
	private static String nameMeFile = "Account/me";
	
	//save my posts
	public static void insertMyPost(Post monPost) {
		ObjectOutputStream oos;
		File mFile = new File(nameMyPostFile);
		final File parent_directory = mFile.getParentFile();
		if (null != parent_directory)
		    parent_directory.mkdirs();
		if (mFile.exists()){
			try
			{
				FileOutputStream mFileOS = new FileOutputStream(mFile, true);
				BufferedOutputStream mBufferedOS = new BufferedOutputStream(mFileOS);
				oos = new ObjectOutputStream(mBufferedOS);
				oos.writeObject(monPost);
				oos.flush();
				oos.close();
			}
			catch (java.io.IOException e) {
				System.out.println("erreur a l'ajout d'un post : " +e);
				e.printStackTrace();
			}
		}
	}
	
	public static Set<Post> selectAllMyPosts() {
		ObjectInputStream ois = null;
		Set<Post> myPosts= new HashSet<Post>();
		File mFichier = new File(nameMyPostFile);
		if (mFichier.exists()){
			try
			{
				FileInputStream mFileIS = new FileInputStream(mFichier);
				BufferedInputStream mBufferedIS = new BufferedInputStream(mFileIS);
				while(mBufferedIS.available() > 0 ){
					ois = new ObjectInputStream(mBufferedIS);
					Post monPost = (Post)ois.readObject();
					String monPostContent = monPost.getContent();
					myPosts.add(monPost);
				}
				if(ois != null)
					ois.close();
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println("Erreur a l'initialisation des posts du profil : " + e);
				 e.printStackTrace();
			}
		}
		return myPosts;
	}
	
	//save their posts
	public static void insertTheirPost(Post sonPost) {
		ObjectOutputStream oos;
		File mFile = new File(nameTheirPostFile);
		final File parent_directory = mFile.getParentFile();
		if (null != parent_directory)
		    parent_directory.mkdirs();
		if (mFile.exists()){
			try
			{
			FileOutputStream mFileOS = new FileOutputStream(mFile, true);
			BufferedOutputStream mBufferedOS = new BufferedOutputStream(mFileOS);
			oos = new ObjectOutputStream(mBufferedOS);
			oos.writeObject(sonPost);
			oos.flush();
			oos.close();
			}
			catch (java.io.IOException e) {
				System.out.println("erreur a l'ajout d'un post etranger : " +e);
				e.printStackTrace();
			}
		}
	}
	
	public static Set<Post> selectAllTheirPosts() {
		ObjectInputStream ois = null;
		Set<Post> myPosts= new HashSet<Post>();
		File mFichier = new File(nameTheirPostFile);
		if (mFichier.exists()){
			try
			{
				FileInputStream mFileIS = new FileInputStream(mFichier);
				BufferedInputStream mBufferedIS = new BufferedInputStream(mFileIS);
				while(mBufferedIS.available() > 0 ){
					ois = new ObjectInputStream(mBufferedIS);
					Post monPost = (Post)ois.readObject();
					String monPostContent = monPost.getContent();
					myPosts.add(monPost);
				}
				if(ois != null)
					ois.close();
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println("Erreur a l'initialisation des posts etrangers du profil : " + e);
				 e.printStackTrace();
			}
		}
		return myPosts;
	}
	
	
	//save my contacts (friends later)
	public static void insertContact(Contact monContact) {
		ObjectOutputStream oos;
		try{
			File mFile = new File(nameContactsFile);
			final File parent_directory = mFile.getParentFile();
			if (null != parent_directory)
			    parent_directory.mkdirs();
			FileOutputStream mFileOS = new FileOutputStream(mFile, true);
			BufferedOutputStream mBufferedOS = new BufferedOutputStream(mFileOS);
			oos = new ObjectOutputStream(mBufferedOS);
			oos.writeObject(monContact);
			oos.flush();
			oos.close();
		}
		catch (java.io.IOException e) {
			System.out.println("erreur a l'ajout d'un contact : " +e);
			e.printStackTrace();
		}
	}
	
	public static Set<Contact> selectAllContacts() {
		ObjectInputStream ois = null;
		Set<Contact> myContacts= new HashSet<Contact>();
		File mFichier = new File(nameContactsFile);
		if (mFichier.exists()){
			try
			{
				FileInputStream mFileIS = new FileInputStream(mFichier);
				BufferedInputStream mBufferedIS = new BufferedInputStream(mFileIS);
				while(mBufferedIS.available() > 0 ){
					ois = new ObjectInputStream(mBufferedIS);
					Contact monContact = (Contact)ois.readObject();
					myContacts.add(monContact);
				}
				if(ois != null)
				ois.close();
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println("Erreur a l'initialisation des contacts du profil : " + e);
				 e.printStackTrace();
			}
		}
		return myContacts;
	}
	
	//save me
	public static void insertMe(Me moi) {
		ObjectOutputStream oos;
		try{
			File mFile = new File(nameMeFile);
			final File parent_directory = mFile.getParentFile();
			if (null != parent_directory)
			    parent_directory.mkdirs();
	         ObjectOutputStream flotEcriture = new ObjectOutputStream(new FileOutputStream(mFile));
	         flotEcriture.writeObject(moi);
	         flotEcriture.close();
		}
		catch (java.io.IOException e) {
			System.out.println("erreur a la sauvegarde de mes parametres : " +e);
			e.printStackTrace();
		}
	}
	
	//get parameters, return null if first connexion
	public static Me selectMe() {
		ObjectInputStream ois = null;
		Me moi= new Me();
		File mFichier = new File(nameMeFile);
		if (mFichier.exists()){
			Me moiSauve = null;
			try
			{
			     ObjectInputStream flotLecture = new ObjectInputStream( new FileInputStream(mFichier));
			     moiSauve=(Me)flotLecture.readObject();
			     flotLecture.close();
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println("Erreur a l'initialisation de mes parametres : " + e);
				 e.printStackTrace();
			}
			return moiSauve;
		}else{
			return null;
		}
		
	}
}
