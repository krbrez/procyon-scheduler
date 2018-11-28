package procyonScheduler.model;

import java.util.GregorianCalendar;
import java.util.Random;

public class Meeting {
	String label; // the name or email address provided by a participant when
					// they book a meeting
	GregorianCalendar dateTime; // the date and time of the meeting
	boolean available; // is the meeting currently booked or not
	Schedule schedule; // all meetings are within a schedule
	String participantSecretCode; // the secret code that is needed to change a
									// meeting, also functions as meeting id

	// constructor for getting an existing meeting
	public Meeting(String label, GregorianCalendar dateTime, boolean available, Schedule schedule,
			String participantSecretCode) {
		this.label = label;
		this.dateTime = dateTime;
		this.available = available;
		this.schedule = schedule;
		this.participantSecretCode = participantSecretCode;
	}

	// constructor for making a new meeting
	public Meeting(String label, GregorianCalendar dateTime, boolean available, Schedule schedule) {
		this.label = label;
		this.dateTime = dateTime;
		this.available = available;
		this.schedule = schedule;
		this.participantSecretCode = "";
		Random r = new Random();
		for (int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if (n < 10) {
				this.participantSecretCode += Integer.toString(n);
			} else if (n < 36) {
				this.participantSecretCode += (char) (n + 55);
			} else {
				this.participantSecretCode += (char) (n + 60);
			}
		}
	}

	// relevant setters
	public void setAvailable() {
		this.available = true;
	}

	// relevant getters
	public boolean getAvailable() {
		return available;
	}

	public String getParticipantSecretCode() {
		return participantSecretCode;
	}

	public void setParticipantSecretCode(String participantSecretCode) {
		this.participantSecretCode = participantSecretCode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public GregorianCalendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(GregorianCalendar dateTime) {
		this.dateTime = dateTime;
	}

	public Schedule getSchedule() {
		return schedule;
	}

}
