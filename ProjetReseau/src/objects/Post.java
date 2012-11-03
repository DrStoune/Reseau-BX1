package objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;



public class Post implements Serializable {

		private int postID;
		private String postOwner;
		private String postContent;
		private String postDate;
		private Date date;
		
		public Post(String owner, String content, GregorianCalendar GCdate){			 
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dateDate = GCdate.getTime();
			postDate = dateFormat.format(dateDate);
			postOwner = owner;
			postContent = content;
		}
		
		public String getDate(){
			return postDate;
		}
		
		public void setDate(String date){
			this.postDate = date;
		}
		
		public void setDate(GregorianCalendar GCdate){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dateDate = GCdate.getTime();
			this.postDate = dateFormat.format(dateDate);
		}
		
		public String getContent(){
			return postContent;
		}
		
		public void setContent(String content){
			this.postContent = content;
		}
}
