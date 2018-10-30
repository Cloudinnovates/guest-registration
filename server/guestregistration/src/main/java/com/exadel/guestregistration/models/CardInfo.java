package com.exadel.guestregistration.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Document(collection="cardinfo")    
public class CardInfo {
	
	@Id
	@GeneratedValue   
    private String id;
	
	@NotBlank                  
    @Size(max=100)
    @Indexed(unique=false)     
    private int cardNumber;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String firstName; 
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String lastName;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String holderType;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String location; 
	
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private Date issued;  
	
	@JsonInclude(JsonInclude.Include.NON_NULL)    
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = false)
	private Date returned; 

    
	public CardInfo(String id, int cardNumber, String firstName, String lastName, String holderType,                              
			       String location, Date issued, Date returned) {  
		this.id = id;
		this.cardNumber = cardNumber; 
		this.firstName = firstName;
		this.lastName = lastName;
		this.holderType = holderType;
		this.location = location;
		this.issued = issued;
		this.returned = returned; 
	}
	
	public CardInfo() {   
	}
	
	
	@Override
	public final boolean equals(Object obj) {    
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}
		
		if (obj instanceof CardInfo) {
			CardInfo givenCardInfo = (CardInfo) obj; 
			return Objects.equals(this.firstName, givenCardInfo.firstName) 
					 && Objects.equals(this.lastName, givenCardInfo.lastName);                   	  
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

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
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

	public String getHolderType() {
		return holderType;
	}

	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getIssued() {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String convertedIssueDate = df.format(issued);

		return convertedIssueDate; 
	}

	public void setIssued(String issued) {

		if (issued.matches("([0-9]{4})-([0-9]{2}-([0-9]{2}))")) { 

			Date issueDate = new Date();

			this.issued = issueDate;

		} else {

			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			Date myDate = null;
			try {
				myDate = sourceFormat.parse(issued);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			this.issued = myDate;   

		}

	}
	
	
	public String getReturned() {

		if (returned == null) {
			return null;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String convertedReturnDate = df.format(returned);

		return convertedReturnDate;  
	}
	
	
	public void setReturned(String returned) {

		if (returned.matches("([0-9]{4})-([0-9]{2}-([0-9]{2}))")) {

			Date returnedDate = new Date();
			
			this.returned = returnedDate;  

		} else {

			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");   

			Date myDate = null;
			try {
				myDate = sourceFormat.parse(returned);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			this.returned = myDate;   

		}

	}

	@Override
	public String toString() {
		return "Event [id = " + id + ", cardNumber = " + cardNumber + ", firstName = " + firstName + ", " 
				        + "lastName = " + lastName + ", holderType = " + holderType + "]";   
	}
	
	
    
} 
