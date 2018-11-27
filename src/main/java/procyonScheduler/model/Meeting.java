package procyonScheduler.model;

import java.util.GregorianCalendar;

public class Meeting {
	int id; 						//unique ID to reference meeting object 
	String label;					//the name or email address provided by a participant when they book a meeting
	GregorianCalendar time; 		//the time of the meeting
	GregorianCalendar date; 		//the date of the meeting
	boolean available; 				//is the meeting currently booked or not
	Schedule schedule;  			//all meetings are within a schedule
	String participantSecretCode; 	//the secret code that is needed to change a meeting
	
	public Meeting(int id, String label, GregorianCalendar time, GregorianCalendar date, boolean available, Schedule schedule, String participantSecretCode) {
		this.id = id;
		this.label = label; //is this optional?
		this.time = time;
		this.date = date;
		this.available = available;
		this.schedule = schedule;
		this.participantSecretCode = participantSecretCode;
	}

}
