package procyonScheduler.model;

import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Class to represent individual meetings on the schedule
 *
 */
public class Meeting  implements Comparable<Meeting> {
	String id;	// the ID for the meeting
	String label; // the name or email address provided by a participant when
					// they book a meeting
	GregorianCalendar dateTime; // the date and time of the meeting
	boolean available; // is the meeting currently booked or not
	String schedule; // all meetings are within a schedule, this is the ID to refer to it
	String participantSecretCode; // the secret code that is needed to change a
									// meeting, also functions as meeting id

	/**
	 * Constructor for getting an existing meeting, used when a meeting already
	 * exists and just needs to be represented
	 * 
	 * @param id
	 *            The id for the Meeting
	 * @param label
	 *            The label the user wants for the meeting
	 * @param dateTime
	 *            The date and time the meeting starts at (end time managed by
	 *            schedule)
	 * @param available
	 *            A boolean that is true if the meeting is open and false if the
	 *            meeting is closed
	 * @param schedule
	 *            The Id of the schedule the Meeting is attached to
	 * @param participantSecretCode
	 *            The secret code for editing this meeting
	 */
	public Meeting(String id, String label, GregorianCalendar dateTime, boolean available, String schedule,
			String participantSecretCode){
		this.id = id;
		this.label = label;
		this.dateTime = dateTime;
		this.available = available;
		this.schedule = schedule;
		this.participantSecretCode = participantSecretCode;
	}

	/**
	 * Constructor for making a new meeting.  Generates params that are not provided
	 * @param label The label the user wants for the meeting
	 * @param dateTime The date and time the meeting starts at (end time managed by schedule)
	 * @param available A boolean that is true if the meeting is open and false if the meeting is closed
	 * @param schedule The Id of the schedule the meeting is attached to
	 */
	public Meeting(String label, GregorianCalendar dateTime, boolean available, String schedule) {
		this.label = label;
		this.dateTime = dateTime;
		this.available = available;
		this.schedule = schedule;
		// Random secret code generator
		this.participantSecretCode = "";
		Random r = new Random();
		for (int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if (n < 10) {
				this.participantSecretCode += Integer.toString(n);
			} else if (n < 36) {
				this.participantSecretCode += (char) (n + 55);
			} else {
				this.participantSecretCode += (char) (n + 61);
			}
		}
		// Random id generator
		this.id = "";
		Random m = new Random();
		for (int i = 0; i < 16; i++) {
			int n = m.nextInt(62);
			if (n < 10) {
				this.id += Integer.toString(n);
			} else if (n < 36) {
				this.id += (char) (n + 55);
			} else {
				this.id += (char) (n + 61);
			}
		}
	}
	
	
	/**
	 * clear label, reroll secret code (basically, make the changes needed when a meeting is cancelled)
	 */
	public void cancel() {
		this.label = "";
		
		this.participantSecretCode = "";
		Random r = new Random();
		for (int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if (n < 10) {
				this.participantSecretCode += Integer.toString(n);
			} else if (n < 36) {
				this.participantSecretCode += (char) (n + 55);
			} else {
				this.participantSecretCode += (char) (n + 61);
			}
		}
	}
	
	
	
	/**
	 * A compare to method for sorting, comparison is by time, then date for dumb HTML reasons
	 * @param other The other meeting to be compared to 
	 * @returns an integer representing comparison
	 */
	public int compareTo(Meeting other) {
		int diff = 0;
		
		int tHour = this.dateTime.get(GregorianCalendar.HOUR_OF_DAY);
		int oHour = other.getDateTime().get(GregorianCalendar.HOUR_OF_DAY);
		int tMin = this.dateTime.get(GregorianCalendar.MINUTE);
		int oMin = other.getDateTime().get(GregorianCalendar.MINUTE);
		
		if(tHour > oHour) diff = 1;
		else if(tHour < oHour) diff = -1;
		else {
			if(tMin > oMin) diff = 1;
			else if(tMin < oMin) diff = -1;
			else diff = this.dateTime.compareTo(other.getDateTime());
		}
		
		return diff;
	}

	/**
	 * Setter for available 
	 * @param avail True if the meeting is open, false if closed
	 */
	public void setAvailable(boolean avail) {
		this.available = avail;
	}

	/**
	 * Getter for available
	 * @return True if the meeting is open, false if closed
	 */
	public boolean getAvailable() {
		return available;
	}

	/**
	 * Getter for secret code
	 * @return The secret code to edit this meeting
	 */
	public String getParticipantSecretCode() {
		return participantSecretCode;
	}

	/**
	 * Setter for secret code
	 * @param participantSecretCode The secret code to edit this meeting
	 */
	public void setParticipantSecretCode(String participantSecretCode) {
		this.participantSecretCode = participantSecretCode;
	}

	/**
	 * Getter for label
	 * @return The label for this meeting
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setter for label
	 * @param label The label for this meeting
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter for date time
	 * @return The date and time the meeting starts at
	 */
	public GregorianCalendar getDateTime() {
		return dateTime;
	}

	/**
	 * The setter for date time
	 * @param dateTime The date and time the meeting starts at
	 */
	public void setDateTime(GregorianCalendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Getter for schedule ID
	 * @return The ID of the schedule the meeting is associated with
	 */
	public String getSchedule() {
		return schedule;
	}

	/**
	 * Getter for the ID
	 * @return The ID for the meeting
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * A toString for testing purposes
	 */
	@Override
	public String toString() {
		String printThis = "id: " + this.id + " label: " + this.label 
				+ " date: " + Integer.toString(this.dateTime.get(GregorianCalendar.YEAR)) 
				+ "-" + Integer.toString(this.dateTime.get(GregorianCalendar.MONTH)) 
				+ "-" + Integer.toString(this.dateTime.get(GregorianCalendar.DAY_OF_MONTH))
				+ " time: " + Integer.toString(this.dateTime.get(GregorianCalendar.HOUR_OF_DAY)) 
				+ ":" + Integer.toString(this.dateTime.get(GregorianCalendar.MINUTE))
				+ " schedule: " + this.schedule + " code: " + this.participantSecretCode;
		return printThis;
	}

}
