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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Entity
@Document(collection="event")    
public class Event {
	
	@Id
	@GeneratedValue   
    private String id;
	
	@NotBlank                  
    @Size(max=100)
    @Indexed(unique=false)     
    private String eventName;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String manager; 
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String location;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private Date dateFrom;
	
	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private Date dateTo;   
	
	@NotBlank                        
    @Size(max=100)
	@Indexed(unique=false)
    private int participantNo;    

	@NotBlank
    @Size(max=100)
	@Indexed(unique=false)
    private String eventType;

	@NotNull
	private boolean isPrivate;

	public Event(@NotBlank @Size(max = 100) String eventName, @NotBlank @Size(max = 100) String manager, @NotBlank @Size(max = 100) String location, @NotBlank @Size(max = 100) Date dateFrom, @NotBlank @Size(max = 100) Date dateTo, @NotBlank @Size(max = 100) int participantNo, @NotBlank @Size(max = 100) String eventType, @NotNull boolean isPrivate) {
		this.eventName = eventName;
		this.manager = manager;
		this.location = location;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.participantNo = participantNo;
		this.eventType = eventType;
		this.isPrivate = isPrivate;
	}

	public Event() {
	}


	@Override
	public final boolean equals(Object obj) {    
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}
		
		if (obj instanceof Event) {
			Event givenEvent = (Event) obj; 
			return Objects.equals(this.eventName, givenEvent.eventName) 
					 && Objects.equals(this.manager, givenEvent.manager);               	  
		}
		return false;                                       
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.eventName, this.manager);                                      
		 
	} 
    

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getManager() {
		return manager;
	}
	
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {   
		this.location = location;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean aPrivate) {
		isPrivate = aPrivate;
	}

	public Date dateFrom() {
		return dateFrom;            
	}

	public String getDateFrom() {                                             
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    String convertedDateFrom = df.format(dateFrom);  
		
		return convertedDateFrom;   
	}    


	public void setDateFrom(String dateFrom) {	                               
		              
		    if (dateFrom.matches("([0-9]{2})-([0-9]{2}-([0-9]{4}))"))  {
		    	
		    	DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		    	sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				Date myDate = null;
				try {
					myDate = sourceFormat.parse(dateFrom);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		    	
    			this.dateFrom = myDate;  
            }
		    else {
		        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		        formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
		        Date myDateFrom = null;
				try {
					myDateFrom = formatter.parse(dateFrom);
				} catch (ParseException e) {
					e.printStackTrace();
				}  
			
	    		this.dateFrom = myDateFrom;    
    		
		    } 
    			
	} 
	
	
	public Date dateTo() {
		return dateTo;
	}


	public String getDateTo() {	                                               
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");             
	    String convertedDateTo = df.format(dateTo);
		
		return convertedDateTo;   
	}


	public void setDateTo(String dateTo) {                                         
		    
            if (dateTo.matches("([0-9]{2})-([0-9]{2}-([0-9]{4}))"))  {
		    	
		    	DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		    	sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		    	
				Date myDate = null;
				try {
					myDate = sourceFormat.parse(dateTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		    	
    			this.dateTo = myDate;    
            }
            else {
		        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));             
		        Date myDateTo = null;
				try {
					myDateTo = formatter.parse(dateTo);
				} catch (ParseException e) {
					e.printStackTrace();
				} 
			
				this.dateTo = myDateTo; 
			
            }
		 	
	}


	public int getParticipantNo() {
		return participantNo;
	}

	public void setParticipantNo(int participantNo) {
		this.participantNo = participantNo;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "Event [id = " + id + ", eventName = " + eventName + ", manager = " + manager + ", location = " + location + "]";  
	}
	
	
    
}  

