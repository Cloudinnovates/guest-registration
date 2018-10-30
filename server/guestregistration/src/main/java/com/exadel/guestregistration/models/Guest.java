package com.exadel.guestregistration.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Document(collection = "guests") 
public class Guest {

	@Id
	@GeneratedValue
	private String id;

	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String firstName;

	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String lastName; 

	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String visitType;
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private Date arrived;  
	
	@JsonInclude(JsonInclude.Include.NON_NULL)    
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private Date left;    
	  
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private int cardNumber;  

	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String location;
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String manager;
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String comment;
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String cardId;
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private String guestId;
	
	public Guest(String id, String firstName, String lastName, String visitType, Date arrived, Date left, int cardNumber,              
					 String location, String manager, String comment, String cardId, String guestId) {   	
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName; 
		this.visitType = visitType;
		this.arrived = arrived;        
		this.left = left;
		this.cardNumber = cardNumber;             
		this.location = location; 
		this.manager = manager; 
		this.comment = comment;
		this.cardId = cardId; 
		this.guestId = guestId;  
	}  

	public Guest(String id, String visitType, Date arrived, Date left, String comment, String cardId, String guestId) {                 
		this.id = id;
		this.visitType = visitType;	
		this.arrived = arrived;   
		this.left = left; 
		this.comment = comment; 
		this.cardId = cardId;  
		this.guestId = guestId;         
	} 
	


	public Guest() { 
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj instanceof Guest) {
			Guest givenGuest = (Guest) obj;
			return Objects.equals(this.firstName, givenGuest.firstName)
					&& Objects.equals(this.lastName, givenGuest.lastName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.firstName, this.lastName);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}   

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}  
	

	public String getArrived() {          
	    
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"); 
	    String convertedArrivalDate = df.format(arrived);   
		
		return convertedArrivalDate;    
	}  
   
	
	public void setArrived(String arrived) {

		if (arrived.matches("([0-9]{4})-([0-9]{2}-([0-9]{2}))")) {

			Date arrivalDate = new Date();

			this.arrived = arrivalDate;

		} else {

			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			Date myDate = null;
			try {
				myDate = sourceFormat.parse(arrived);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			this.arrived = myDate;

		}

	}  
	
	
	public String getLeftForDisplay() {	  
		
		if (left == null) {  
			return null; 
		}   
		         
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");               
	    String convertedLeavingDate = df.format(left);

		return convertedLeavingDate;  
	}
	
	public String getLeft() {	 
		
		if (left == null) {  
			return null; 
		}   
		         
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");                   
	    String convertedLeavingDate = df.format(left);

		return convertedLeavingDate;  
	}
	

	public void setLeft(String left) {     
	    
		if (left.matches("([0-9]{4})-([0-9]{2}-([0-9]{2}))")) { 

			Date arrivalDate = new Date();
            System.out.println("This is left date: " + arrivalDate); 
			this.left = arrivalDate;

		} else {

			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");   

			Date myDate = null;
			try {
				myDate = sourceFormat.parse(left);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			this.left = myDate;  

		}
	 	
    } 
	

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;    
	}        

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	} 

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	} 

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {    
		this.guestId = guestId;
	}

	@Override
	public String toString() {
		return "Guest [id = " + id + ", firstName = " + firstName + ", lastName = " + lastName + "]";   
	}

}  
